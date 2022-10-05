package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.controller;


import com.bithumbsystems.lrc.management.api.core.config.properties.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.exception.LrcException;
import com.bithumbsystems.lrc.management.api.v1.file.service.FileService;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.model.request.ReviewEstimateRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.service.ReviewEstimateService;
import com.bithumbsystems.persistence.mongodb.chat.model.enums.FileStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode.INVALID_FILE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/lrcmanagment/project")
@Tag(name = "검토 평가", description = "검포 평가 API")
@Slf4j
public class ReviewEstimateController {

    private final ReviewEstimateService reviewEstimateService;
    private final AwsProperties awsProperties;
    private final FileService fileService;
    /**
     * 검토 평가 id로 찾기
     * @param projectId
     * @return ReviewEstimateResponse Object
     */
    @GetMapping("/review-estimate/{projectId}")
    @Operation(summary = "거래지원 관리 - 검토 평가 id로 찾기", description = "projectId를 이용하여 검토 평가 정보를 조회합니다.", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 검토 평가 > 검색")
    public ResponseEntity<Mono<?>> getReviewEstimate(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH)
                                                         @PathVariable("projectId") String projectId) {
        return ResponseEntity.ok().body(reviewEstimateService.findByUseData(projectId)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 검토 평가 여러개 저장 및 업데이트 ( 이거로 사용함 )
     * 리스트로 받아서 파일 여부 확인 하여 저장 해야 함
     * @param reviewEstimateList
     * @return ReviewEstimateResponse Object
     */
    @PostMapping(value = "/review-estimate/upload/s3", consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "거래지원 관리 - 검토 평가 여러개 저장 및 업데이트", description = "projectId를 이용하여 검토 평가 정보를 여러개 저장합니다.", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 검토평가 > 파일 저장")
    public ResponseEntity<Mono<SingleResponse>> createReviewEstimateFile(@ModelAttribute(value = "reviewEstimateList") ReviewEstimateRequest reviewEstimateList,
                                                                         @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(reviewEstimateService.saveAll(reviewEstimateList, account)
                .map(c -> new SingleResponse(c)));
    }

    /**
     * 검토 평가 삭제
     * @param projectId
     * @param id
     * @return ReviewEstimateResponse Object
     */
    @DeleteMapping("/review-estimate/{projectId}/{id}")
    @Operation(summary = "거래지원 관리 - 검토 평가 삭제", description = "id를 이용하여 검토 평가 정보를 삭제합니다.", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 검토 평가 > 삭제")
    public ResponseEntity<Mono<?>> getReviewEstimate(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH) @PathVariable("projectId") String projectId,
                                                     @Parameter(name = "id", description = "검토평가 ID", in = ParameterIn.PATH) @PathVariable("id") String id,
                                                     @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(reviewEstimateService.deleteById(projectId, id, account)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 검토평가 파일 다운로드
     * @param projectId
     * @param fileKey
     * @param account
     * @return
     */
    @GetMapping(value = "/review-estimate/file/{projectId}/{id}/{fileKey}", produces = APPLICATION_OCTET_STREAM_VALUE)
    public Mono<ResponseEntity<?>> reviewDownload(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH) @PathVariable("projectId") String projectId,
                                                      @Parameter(name = "id", description = "검토평가의 id", in = ParameterIn.PATH) @PathVariable("id") String id,
                                                      @Parameter(name = "fileKey", description = "project 의 fileKey", in = ParameterIn.PATH) @PathVariable String fileKey,
                                                      @Parameter(hidden = true) @CurrentUser Account account) {


        return reviewEstimateService.findById(id)
                .flatMap(result -> {
                   // 검토 평가 파일 다운로드는 내가 올린 파일은 다운로드 받지 못한다.
                    log.debug("create id => {}, current id => {}", result.getCreateAdminAccountId(), account.getAccountId());
                    if (result.getCreateAdminAccountId().equals(account.getAccountId())) {
                        return Mono.error(new LrcException(INVALID_FILE));
                    }

                    // 파일의 상태가 CLEAN 상태가 아니라면 다운로드 받지 못한다.
                    if (result.getFileStatus() != FileStatus.CLEAN) {
                        return Mono.error(new LrcException(INVALID_FILE));
                    }
                    // 파일 다운로드가 가능한다.
                    return fileService.download(fileKey, awsProperties.getBucket())
                            .log()
                            .map(inputStream -> {
                                log.debug("finaly result...here");
                                String fileName = "common-file-name-user-define.txt";  // 파일명은 클라이언트에서 정한다.
                                HttpHeaders headers = new HttpHeaders();
                                headers.setContentDispositionFormData(fileName.toString(), fileName.toString());
                                ResponseEntity<?> entity = ResponseEntity.ok().cacheControl(CacheControl.noCache())
                                        .headers(headers)
                                        .body(new InputStreamResource(inputStream));
                                return entity;
                            });
                });
    }
}
