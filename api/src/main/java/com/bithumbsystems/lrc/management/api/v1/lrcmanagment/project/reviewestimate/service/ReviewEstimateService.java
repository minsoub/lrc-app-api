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
import reactor.core.scheduler.Schedulers;

import java.nio.ByteBuffer;
import java.text.Normalizer;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewEstimateService {

    private final ReviewEstimateDomainService reviewEstimateDomainService;

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
    public Mono<List<ReviewEstimateResponse>> saveAll(Flux<ReviewEstimateRequest> reviewEstimateRequest) {
        return reviewEstimateRequest
                .flatMap(reviewEstimate -> {
                    if(reviewEstimate.getFilePart() != null) {  //첨부파일 확인
                        return DataBufferUtils.join(reviewEstimate.getFilePart().content())
                                .flatMap(dataBuffer -> {
                                    ByteBuffer buf = dataBuffer.asByteBuffer();
                                    String fileKey = UUID.randomUUID().toString();
                                    String fileName = reviewEstimate.getFilePart().filename();
                                    Long fileSize = (long) buf.array().length;
                                    log.info("byte size ===>  {}   :   {}   :   {} : ", fileKey, fileName, fileSize);

                                    return fileService.upload(fileKey, fileName, fileSize, awsProperties.getBucket(), buf)
                                            .flatMap(res -> {
                                                log.info("service upload res   =>       {}", res);
                                                log.info("service upload fileName   =>       {}", fileName.toString());
                                                File info = File.builder()
                                                        .fileKey(fileKey)
                                                        .fileName(Normalizer.normalize(fileName, Normalizer.Form.NFC))
                                                        .createdAt(new Date())
                                                        .createdId("test")
                                                        .delYn(false)
                                                        .build();

                                                return fileService.save(info);
                                            })
                                            .publishOn(Schedulers.boundedElastic())
                                            .flatMap(file -> {
                                                reviewEstimate.setFileKey(file.getFileKey());
                                                reviewEstimate.setReference(file.getFileName());
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
