package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.service;

import com.bithumbsystems.lrc.management.api.core.config.property.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.file.service.FileService;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.mapper.ReviewEstimateMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.model.request.ReviewEstimateRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.model.response.ReviewEstimateResponse;
import com.bithumbsystems.persistence.mongodb.file.model.entity.File;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.reviewestimate.service.ReviewEstimateDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewEstimateService {

    private final ReviewEstimateDomainService reviewEstimateDomainService;

    private final AwsProperties awsProperties;
    private final S3AsyncClient s3AsyncClient;
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
     * @param projectId
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
    public Mono<List<ReviewEstimateResponse>> saveAll(Flux<ReviewEstimateRequest> reviewEstimateRequest) {
        return reviewEstimateRequest
                .flatMap(reviewEstimate -> {
                    if(reviewEstimate.getFilePart() != null) {  //첨부파일 확인
                        AtomicReference<String> fileKey = new AtomicReference<>();
                        AtomicReference<String> fileName = new AtomicReference<>();
                        AtomicReference<Long> fileSize = new AtomicReference<>();

                        return DataBufferUtils.join(reviewEstimate.getFilePart().content())
                                .flatMap(dataBuffer -> {
                                    ByteBuffer buf = dataBuffer.asByteBuffer();
                                    fileSize.set((long) buf.array().length);
                                    fileKey.set(UUID.randomUUID().toString());
                                    fileName.set(reviewEstimate.getFilePart().filename());
                                    log.info("byte size ===>  {}  {}  {} : ", buf.array().length, fileKey.toString(), fileName.toString());

                                    return fileService.upload(fileKey.toString(), fileName.toString(), fileSize.get(), awsProperties.getBucket(), buf)
                                            .flatMap(res -> {
                                                log.info("service upload res   =>       {}", res);
                                                log.info("service upload fileName   =>       {}", fileName.toString());
                                                File info = File.builder()
                                                        .fileKey(fileKey.toString())
                                                        .fileName(fileName.toString())
                                                        .createdAt(new Date())
                                                        .createdId("test")
                                                        .delYn(false)
                                                        .build();

                                                fileService.save(info);
                                                reviewEstimate.setReferenceFile(info.getFileKey());
                                                return reviewEstimateDomainService.save(ReviewEstimateMapper.INSTANCE.requestToReviewEstimate(reviewEstimate));
                                            });
                                });
                    } else {
                        return reviewEstimateDomainService.save(ReviewEstimateMapper.INSTANCE.requestToReviewEstimate(reviewEstimate));
                    }
                }).collectList()
                .then(this.findByProjectId("PRJ001"));  //프로젝트 명은 바꾸어야 함
    }
}
