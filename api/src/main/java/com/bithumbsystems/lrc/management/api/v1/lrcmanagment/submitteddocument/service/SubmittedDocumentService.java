package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.service;

import com.bithumbsystems.lrc.management.api.core.config.property.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.file.service.FileService;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.mapper.SubmittedDocumentMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.model.request.SubmittedDocumentRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.model.response.SubmittedDocumentResponse;
import com.bithumbsystems.persistence.mongodb.file.model.entity.File;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.service.SubmittedDocumentDomainService;
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
public class SubmittedDocumentService {

    private final SubmittedDocumentDomainService submittedDocumentDomainService;
    private final AwsProperties awsProperties;
    private final FileService fileService;

    /**
     * 제출 서류 관리 id로 찾기
     * @param projectId
     * @return ReviewEstimateResponse Object
     */
    public Mono<List<SubmittedDocumentResponse>> findByProjectId(String projectId) {
        return submittedDocumentDomainService.findByProjectId(projectId)
                .map(SubmittedDocumentMapper.INSTANCE::submittedDocumentResponse)
                .collectList()
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 제출 서류 관리 여러개 저장 및 업데이트
     * @param submittedDocumentRequest
     * @return SubmittedDocumentResponse Object
     */
    @Transactional
    public Mono<List<SubmittedDocumentResponse>> saveAll(Flux<SubmittedDocumentRequest> submittedDocumentRequest) {
        return submittedDocumentRequest
                .flatMap(submittedDocument -> {
                    if(submittedDocument.getFilePart() != null) {  //첨부파일 확인
                        return DataBufferUtils.join(submittedDocument.getFilePart().content())
                                .flatMap(dataBuffer -> {
                                    ByteBuffer buf = dataBuffer.asByteBuffer();
                                    String fileKey = UUID.randomUUID().toString();
                                    String fileName = submittedDocument.getFilePart().filename();
                                    Long fileSize = (long) buf.array().length;
                                    log.info("byte size ===>  {}   :   {}   :   {} : ", fileKey, fileName, fileSize);

                                    return fileService.upload(fileKey, fileName, fileSize, awsProperties.getBucket(), buf)
                                            .publishOn(Schedulers.boundedElastic())
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
                                                submittedDocument.setFileKey(file.getFileKey());
                                                submittedDocument.setFileName(file.getFileName());
                                                return submittedDocumentDomainService.save(SubmittedDocumentMapper.INSTANCE.requestToSubmittedDocument(submittedDocument));
                                            });
                                });
                    } else {
                        return submittedDocumentDomainService.save(SubmittedDocumentMapper.INSTANCE.requestToSubmittedDocument(submittedDocument));
                    }
                }).collectList()
                .then(this.findByProjectId("PRJ001"));  //프로젝트 명은 바꾸어야 함
    }
}
