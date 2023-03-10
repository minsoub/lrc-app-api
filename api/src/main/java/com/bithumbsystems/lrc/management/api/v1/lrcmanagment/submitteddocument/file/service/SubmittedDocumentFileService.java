package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.service;

import com.bithumbsystems.lrc.management.api.core.config.properties.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.core.util.AES256Util;
import com.bithumbsystems.lrc.management.api.v1.file.service.FileService;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.listener.HistoryDto;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.exception.SubmittedDocumentFileException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.mapper.SubmittedDocumentFileMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.model.request.SubmittedDocumentFileRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.model.response.SubmittedDocumentFileResponse;
import com.bithumbsystems.persistence.mongodb.file.model.entity.File;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.file.model.entity.SubmittedDocumentFile;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.file.service.SubmittedDocumentFileDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.model.enums.SubmittedDocumentEnums;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final ApplicationEventPublisher applicationEventPublisher;
    private final AwsProperties awsProperties;
    private final FileService fileService;

    /**
     * ?????? ?????? ?????? id, type ?????? file ??????
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
                            .fileStatus(result.getFileStatus())
                            .email(AES256Util.decryptAES(awsProperties.getKmsKey(), result.getEmail()))
                            .createDate(result.getCreateDate())
                            .createAdminAccountId(result.getCreateAdminAccountId())
                            .createAccountId(result.getCreateAccountId())
                            .build());
                })
                .collectSortedList(Comparator.comparing(SubmittedDocumentFileResponse::getCreateDate).reversed());
    }

    /**
     * ?????? ?????? ?????? file ??????
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
                                                    historyLogSend(request.getProjectId(), "????????????", request.getType(), "????????????(?????????)", Normalizer.normalize(fileName, Normalizer.Form.NFC), account);
                                                    request.setFileKey(file.getFileKey());
                                                    request.setFileName(file.getFileName());
                                                    return submittedDocumentFileDomainService.save(
                                                            SubmittedDocumentFile.builder()
                                                                    .projectId(request.getProjectId())
                                                                    .type(request.getType())
                                                                    .fileKey(request.getFileKey())
                                                                    .fileName(request.getFileName())
                                                                    .email(AES256Util.encryptAES(awsProperties.getKmsKey(), account.getEmail(), awsProperties.getSaltKey(), awsProperties.getIvKey()))
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
     * ?????? ?????? ?????? file ??????
     * @param id
     * @return SubmittedDocumentResponse Object
     */
    public Mono<Void> deleteSubmittedDocumentFile(String id) {
        return submittedDocumentFileDomainService.findSubmittedDocumentFileById(id)
                .flatMap(submittedDocumentFileDomainService::deleteSubmittedDocumentFile)
                .switchIfEmpty(Mono.error(new SubmittedDocumentFileException(ErrorCode.FAIL_UPDATE_CONTENT)));
    }

    /**
     * ?????? ???????????? ??????.
     *
     * @param projectId
     * @param menu
     * @param type
     * @param taskHistory
     * @param account
     * @return
     */
    private void historyLogSend(String projectId, String menu, SubmittedDocumentEnums type, String taskHistory, String item, Account account) {
        String subject = "";
        if (type.equals(SubmittedDocumentEnums.IPO_APPLICATION)) {
            subject = "???????????? ?????????";
        } else if(type.equals(SubmittedDocumentEnums.COLLECT_CONFIRM)) {
            subject = "???????????? ?????? ??? ???????????????";
        } else if(type.equals(SubmittedDocumentEnums.PROJECT_WHITEPAPER)) {
            subject = "???????????? ??????";
        } else if(type.equals(SubmittedDocumentEnums.TECH_REVIEW_REPORT)) {
            subject = "?????????????????????";
        } else if(type.equals(SubmittedDocumentEnums.TOKEN_DIVISION_PLAN)) {
            subject = "?????? ?????? ??? ?????? ?????????";
        } else if(type.equals(SubmittedDocumentEnums.LEGAL_REVIEW)) {
            subject = "?????????????????????";
        } else if(type.equals(SubmittedDocumentEnums.BUSINESS_LICENSE)) {
            subject = "??????????????????";
        } else if(type.equals(SubmittedDocumentEnums.ETHICAL_WRITE_AUTH)) {
            subject = "???????????????";
        } else if(type.equals(SubmittedDocumentEnums.REGULATORY_COMPLIANCE)) {
            subject = "???????????? ?????????";
        } else if(type.equals(SubmittedDocumentEnums.ETC)) {
            subject = "??????";
        } else if (type.equals(SubmittedDocumentEnums.SHAREHOLDER)) {
            subject = "????????????";
        }
        applicationEventPublisher.publishEvent(
                HistoryDto.builder()
                        .projectId(projectId)
                        .menu(menu)
                        .subject(subject)
                        .taskHistory(taskHistory)
                        .item(item)
                        .email(account.getEmail())
                        .accountId(account.getAccountId())
                        .build()
        );
    }
}
