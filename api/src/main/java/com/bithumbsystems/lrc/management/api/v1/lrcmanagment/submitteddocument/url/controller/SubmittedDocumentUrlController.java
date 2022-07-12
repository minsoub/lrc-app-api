package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.controller;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.model.request.SubmittedDocumentUrlRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.service.SubmittedDocumentUrlService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.model.enums.SubmittedDocumentEnums;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/lrcmanagment/project")
@Tag(name = "제출 서류 관리 url", description = "제출 서류 관리 API")
public class SubmittedDocumentUrlController {

    private final SubmittedDocumentUrlService submittedDocumentUrlService;

    /**
     * 제출 서류 목록 type 찾기
     * @return SubmittedDocumentResponse Object
     */
    @GetMapping("/submitted-document/url/type")
    @Operation(summary = "거래지원 관리 - 제출 서류 목록", description = "제출 서류 목록 정보를 조회합니다.", tags = "사이트 운영 > 거래지원 관리 > 제출 서류 관리 > url type 검색")
    public ResponseEntity<Mono<?>> getSubmittedDocumentType() {
        return ResponseEntity.ok().body(Mono.just(SubmittedDocumentEnums.values())
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 제출 서류 관리 Project id 으로 url 찾기
     * @param projectId
     * @return SubmittedDocumentResponse Object
     */
    @GetMapping("/submitted-document/url")
    @Operation(summary = "거래지원 관리 - 제출 서류 관리 Project id 으로 url 찾기", description = "projectId를 이용하여 제출 서류 관리 url 정보를 조회합니다.", tags = "사이트 운영 > 거래지원 관리 > 제출 서류 관리 > url 검색")
    public ResponseEntity<Mono<?>> getSubmittedDocumentUrl(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.QUERY)
                                                               @RequestParam() String projectId) {
        return ResponseEntity.ok().body(submittedDocumentUrlService.findByProjectId(projectId)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 제출 서류 관리 url 저장
     * @param submittedDocumentUrlRequest
     * @return SubmittedDocumentResponse Object
     */
    @PostMapping(value = "/submitted-document/url")
    @Operation(summary = "거래지원 관리 - 제출 서류 관리 url 저장", description = "제출 서류 관리 url 정보를 저장 합니다.", tags = "사이트 운영 > 거래지원 관리 > 제출 서류 관리 > url 저장")
    public ResponseEntity<Mono<?>> createSubmittedDocumentUrl(@Parameter(name = "document Object", description = "제출 서류 Model", in = ParameterIn.PATH)
                                                                @RequestBody SubmittedDocumentUrlRequest submittedDocumentUrlRequest,
                                                              @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(submittedDocumentUrlService.saveAll(submittedDocumentUrlRequest, account)
                .map(c -> new SingleResponse(c)));
    }

    /**
     * 제출 서류 관리 url 삭제
     * @return SubmittedDocumentResponse Object
     */
    @DeleteMapping("/submitted-document/url/{id}")
    @Operation(summary = "거래지원 관리 - 제출 서류 관리 url 삭제", description = "제출 서류 관리 url 정보를 삭제 합니다.", tags = "사이트 운영 > 거래지원 관리 > 제출 서류 관리 > url 삭제")
    public ResponseEntity<Mono<?>> deleteSubmittedDocumentUrl(@Parameter(name = "id", description = "id 정보", in = ParameterIn.PATH)
                                                            @PathVariable("id") String id) {
        return ResponseEntity.ok().body(submittedDocumentUrlService.deleteSubmittedDocumentUrl(id).then(
                Mono.just(new SingleResponse()))
        );
    }
}
