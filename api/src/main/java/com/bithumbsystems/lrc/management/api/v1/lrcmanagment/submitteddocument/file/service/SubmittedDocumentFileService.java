package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.service;

import com.bithumbsystems.lrc.management.api.core.config.property.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.core.util.AES256Util;
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
import java.util.Comparator;
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
     * @return ReviewEstimateResponse Object
     */
    public Mono<List<SubmittedDocumentFileResponse>> findByProjectId(String projectId) {
        return submittedDocumentFileDomainService.findByProjectId(projectId)
                .flatMap(result -> {
                    return Mono.just(SubmittedDocumentFileResponse.builder()
                            .id(result.getId())
                            .projectId(result.getProjectId())
                            .type(result.getType())
                            .fileKey(result.getFileKey())
                            .fileName(result.getFileName())
                            .email(AES256Util.decryptAES(awsProperties.getKmsKey(), result.getEmail()))
                            .createDate(result.getCreateDate())
                            .createAdminAccountId(result.getCreateAdminAccountId())
                            .build());
                })
                //.map(SubmittedDocumentFileMapper.INSTANCE::submittedDocumentFileResponse)
                .collectSortedList(Comparator.comparing(SubmittedDocumentFileResponse::getCreateDate))
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 제출 서류 관리 file 저장
     * @param fileRequest
     * @return SubmittedDocumentResponse Object
     */
    @Transactional
    public Mono<SubmittedDocumentFileResponse> saveAll(Mono<SubmittedDocumentFileRequest> fileRequest, Account account) {
        return fileRequest
                .flatMap(request -> {
                            return DataBufferUtils.join(request.getFile().content())
                                    .flatMap(dataBuffer -> {
                                        ByteBuffer buf = dataBuffer.asByteBuffer();
                                        String fileKey = UUID.randomUUID().toString();
                                        String fileName = request.getFile().filename();
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
                                                    request.setFileKey(file.getFileKey());
                                                    request.setFileName(file.getFileName());
                                                    return submittedDocumentFileDomainService.save(
                                                            SubmittedDocumentFile.builder()
                                                                    .projectId(request.getProjectId())
                                                                    .type(request.getType())
                                                                    .fileKey(request.getFileKey())
                                                                    .fileName(request.getFileName())
                                                                    .email(AES256Util.encryptAES(awsProperties.getKmsKey(), account.getEmail(), false))
                                                                    .createDate(LocalDateTime.now())
                                                                    .createAdminAccountId(account.getAccountId())
                                                                    .build()
                                                    );
                                                });
                                    });
                        }
                )
                .map(SubmittedDocumentFileMapper.INSTANCE::submittedDocumentFileResponse);
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
