package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.service;

import com.bithumbsystems.lrc.management.api.core.config.property.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.file.service.FileService;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.mapper.SubmittedDocumentFileMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.model.request.SubmittedDocumentFileRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.model.response.SubmittedDocumentFileResponse;
import com.bithumbsystems.persistence.mongodb.file.model.entity.File;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.file.model.entity.SubmittedDocumentFile;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.file.service.SubmittedDocumentFileDomainService;
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
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmittedDocumentFileService {

    private final SubmittedDocumentFileDomainService submittedDocumentFileDomainService;
    private final AwsProperties awsProperties;
    private final FileService fileService;

    /**
     * 제출 서류 관리 id, type 으로 file 찾기
     * @param projectId
     * @param type
     * @return ReviewEstimateResponse Object
     */
    public Mono<List<SubmittedDocumentFileResponse>> findByProjectIdAndType(String projectId, String type) {
        return submittedDocumentFileDomainService.findByProjectIdAndType(projectId, type)
                .map(SubmittedDocumentFileMapper.INSTANCE::submittedDocumentFileResponse)
                .collectList()
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 제출 서류 관리 file 저장
     * @param submittedDocumentRequest
     * @return SubmittedDocumentResponse Object
     */
    @Transactional
    public Mono<List<SubmittedDocumentFileResponse>> saveAll(Flux<SubmittedDocumentFileRequest> submittedDocumentRequest, Account account) {
        return submittedDocumentRequest
                .flatMap(submittedDocument ->
                        DataBufferUtils.join(submittedDocument.getFilePart().content())
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
                                                return submittedDocumentFileDomainService.save(
                                                        SubmittedDocumentFile.builder()
                                                                .projectId(submittedDocument.getProjectId())
                                                                .type(submittedDocument.getType())
                                                                .fileKey(submittedDocument.getFileKey())
                                                                .fileName(submittedDocument.getFileName())
                                                                .createDate(LocalDateTime.now())
                                                                .createAdminAccountId(account.getAccountId())
                                                                .build()
                                                );
                                            });
                                })
                )
                .flatMap(res ->
                        submittedDocumentFileDomainService.findByProjectIdAndType(res.getProjectId(), res.getType())
                                .map(SubmittedDocumentFileMapper.INSTANCE::submittedDocumentFileResponse)
                ).collectList();
    }

    /**
     * 제출 서류 관리 file 저장
     * @param id
     * @return SubmittedDocumentResponse Object
     */
    public Mono<Void> deleteSubmittedDocumentFile(String id) {
        return submittedDocumentFileDomainService.findSubmittedDocumentFileById(id)
                .flatMap(submittedDocumentFileDomainService::deleteSubmittedDocumentFile);
    }
}
