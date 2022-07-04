package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.service;

import com.bithumbsystems.lrc.management.api.core.config.property.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.file.service.FileService;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.listener.HistoryDto;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.mapper.ReviewEstimateMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.model.request.ReviewEstimateRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.model.response.ReviewEstimateResponse;
import com.bithumbsystems.persistence.mongodb.file.model.entity.File;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.model.entity.History;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.service.HistoryDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.reviewestimate.model.entity.ReviewEstimate;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.reviewestimate.service.ReviewEstimateDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.ByteBuffer;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewEstimateService {

    private final ReviewEstimateDomainService reviewEstimateDomainService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final AwsProperties awsProperties;
    private final FileService fileService;


    /**
     * 검토 평가 id로 찾기
     * @param projectId
     * @return ReviewEstimateResponse Object
     */
    public Mono<List<ReviewEstimateResponse>> findByProjectId(String projectId) {
        return reviewEstimateDomainService.findByProjectId(projectId)
                .map(ReviewEstimateMapper.INSTANCE::reviewEstimateResponse)
                .collectList()
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 검토 평가 여러개 저장 및 업데이트
     * @param reviewEstimateRequest
     * @return ReviewEstimateResponse Object
     */
    /*public Mono<List<ReviewEstimateResponse>> create(String projectId, ReviewEstimateRequest reviewEstimateRequest) {
        return Mono.just(reviewEstimateRequest.getReviewEstimateList())
                .flatMapMany(reviewEstimates -> Flux.fromIterable(reviewEstimates))
                .flatMap(reviewEstimate ->
                        reviewEstimateDomainService.save(ReviewEstimateMapper.INSTANCE.reviewEstimateResponseToRequest(reviewEstimate))
                )
                .then(this.findByProjectId(projectId));
    }*/

    @Transactional
    public Mono<List<ReviewEstimateResponse>> saveAll(ReviewEstimateRequest reviewEstimateRequest, Account account) {
        //List<ReviewEstimateRequest> array = new ArrayList<>();
        //array.addAll(Arrays.asList(reviewEstimateRequest));

        Queue<FilePart> fileList = new LinkedList<FilePart>();
        if (reviewEstimateRequest.getFile() != null)
            fileList.addAll(reviewEstimateRequest.getFile());

        return  Flux.fromIterable(reviewEstimateRequest.getNo())
                .flatMap(index -> {

                    String id = reviewEstimateRequest.getId().get(index);
                    String projectId = reviewEstimateRequest.getProjectId().get(index);
                    String organization = reviewEstimateRequest.getOrganization().get(index);
                    String result  = reviewEstimateRequest.getResult().get(index);
                    String reference = reviewEstimateRequest.getReference().get(index);
                    String fileKey = reviewEstimateRequest.getFileKey().get(index);
                    Boolean isFile = reviewEstimateRequest.getIsFile().get(index);
                    String fileName = reviewEstimateRequest.getFileName().get(index);

                    // file part
                    if (StringUtils.hasLength(id)) {   // 수정 모드
                        if(isFile) {  //첨부파일 확인
                            return DataBufferUtils.join(fileList.poll().content())
                                    .flatMap(dataBuffer -> {
                                        ByteBuffer buf = dataBuffer.asByteBuffer();
                                        String file_key = UUID.randomUUID().toString();
                                        String file_name = fileName;
                                        Long fileSize = (long) buf.array().length;
                                        log.info("byte size ===>  {}   :   {}   :   {} : ", file_key, file_name, fileSize);

                                        return fileService.upload(file_key, file_name, fileSize, awsProperties.getBucket(), buf)
                                                .flatMap(res -> {
                                                    log.info("service upload res   =>       {}", res);
                                                    log.info("service upload fileName   =>       {}", fileName.toString());

                                                    return reviewEstimateDomainService.findById(id)
                                                            .flatMap(mode -> {
                                                                if (!mode.getOrganization().equals(organization)) {
                                                                    historyLogSend(projectId, "프로젝트 관리>검토 평가", "평가 기관", "수정", account);
                                                                }
                                                                if (!mode.getResult().equals(result)) {
                                                                    historyLogSend(projectId, "프로젝트 관리>검토 평가", "평가 결과", "수정", account);
                                                                }
                                                                if (StringUtils.hasLength(reference)) {
                                                                    if (!mode.getReference().equals(reference)) {
                                                                        historyLogSend(projectId, "프로젝트 관리>검토 평가", "평가 자료", "수정", account);
                                                                    }
                                                                }
                                                                // 파일은 수정이다.
                                                                historyLogSend(projectId, "프로젝트 관리>검토 평가", "검토 평가", "수정", account);

                                                                return reviewEstimateDomainService.save(
                                                                        ReviewEstimate.builder()
                                                                                .id(id)
                                                                                .projectId(projectId)
                                                                                .organization(organization)
                                                                                .result(result)
                                                                                .reference(reference)
                                                                                .fileKey(file_key)
                                                                                .fileName(Normalizer.normalize(file_name, Normalizer.Form.NFC))
                                                                                .build());
                                                            });

                                                });
                                    });
                        } else {
                            return reviewEstimateDomainService.findById(id)
                                            .flatMap(mode -> {
                                                if (!mode.getOrganization().equals(organization)) {
                                                    historyLogSend(projectId, "프로젝트 관리>검토 평가", "평가 기관", "수정", account);
                                                }
                                                if (!mode.getResult().equals(result)) {
                                                    historyLogSend(projectId, "프로젝트 관리>검토 평가", "평가 결과", "수정", account);
                                                }
                                                if (StringUtils.hasLength(reference)) {
                                                    if (!mode.getReference().equals(reference)) {
                                                        historyLogSend(projectId, "프로젝트 관리>검토 평가", "평가 자료", "수정", account);
                                                    }
                                                }
                                                // 파일은 수정이다.
                                                //historyLogSend(projectId, "프로젝트 관리>검토 평가", "검토 평가", "수정", account);

                                                return reviewEstimateDomainService.save(
                                                        ReviewEstimate.builder()
                                                                .id(id)
                                                                .projectId(projectId)
                                                                .organization(organization)
                                                                .result(result)
                                                                .reference(reference)
                                                                .fileKey(fileKey)
                                                                .fileName(fileName)
                                                                .build());
                                            });
                        }
                    }else {     // 신규 등록
                        if(isFile != null) {  //첨부파일 확인
                            return DataBufferUtils.join(fileList.poll().content())
                                    .flatMap(dataBuffer -> {
                                        ByteBuffer buf = dataBuffer.asByteBuffer();
                                        String file_key = UUID.randomUUID().toString();
                                        String file_name = fileName;
                                        Long fileSize = (long) buf.array().length;
                                        log.info("byte size ===>  {}   :   {}   :   {} : ", file_key, file_name, fileSize);

                                        return fileService.upload(file_key, file_name, fileSize, awsProperties.getBucket(), buf)
                                                .flatMap(res -> {
                                                    log.info("service upload res   =>       {}", res);
                                                    log.info("service upload fileName   =>       {}", fileName.toString());

                                                    historyLogSend(projectId, "프로젝트 관리>검토 평가", "평가 기관", "신규등록", account);
                                                    historyLogSend(projectId, "프로젝트 관리>검토 평가", "평가 결과", "신규등록", account);
                                                    if (StringUtils.hasLength(reference))
                                                        historyLogSend(projectId, "프로젝트 관리>검토 평가", "평가 자료", "신규등록", account);
                                                    historyLogSend(projectId, "프로젝트 관리>검토 평가", "검토 평가", "신규등록", account);

                                                    return reviewEstimateDomainService.save(
                                                            ReviewEstimate.builder()
                                                                    .id(UUID.randomUUID().toString())
                                                                    .projectId(projectId)
                                                                    .organization(organization)
                                                                    .result(result)
                                                                    .reference(reference)
                                                                    .fileKey(file_key)
                                                                    .fileName(Normalizer.normalize(file_name, Normalizer.Form.NFC))
                                                                    .build()
                                                    );
                                                });
                                    });
                        } else {
                            historyLogSend(projectId, "프로젝트 관리>검토 평가", "평가 기관", "신규등록", account);
                            historyLogSend(projectId, "프로젝트 관리>검토 평가", "평가 결과", "신규등록", account);
                            if (StringUtils.hasLength(reference))
                                historyLogSend(projectId, "프로젝트 관리>검토 평가", "평가 자료", "신규등록", account);

                            return reviewEstimateDomainService.save(
                                    ReviewEstimate.builder()
                                            .id(UUID.randomUUID().toString())
                                            .projectId(projectId)
                                            .organization(organization)
                                            .result(result)
                                            .reference(reference)
                                            .fileKey(fileKey)
                                            .fileName(fileName)
                                            .build()
                            );
                        }
                    }


                }).collectList()
                .then(this.findByProjectId("PRJ001"));  //프로젝트 명은 바꾸어야 함
    }

    /**
     * 변경 히스토리 저장.
     *
     * @param projectId
     * @param menu
     * @param subject
     * @param taskHistory
     * @param account
     * @return
     */
    private void historyLogSend(String projectId, String menu, String subject, String taskHistory, Account account) {
        applicationEventPublisher.publishEvent(
                HistoryDto.builder()
                        .projectId(projectId)
                        .menu(menu)
                        .subject(subject)
                        .taskHistory(taskHistory)
                        .email(account.getEmail())
                        .accountId(account.getAccountId())
                        .build()
        );
    }
}
