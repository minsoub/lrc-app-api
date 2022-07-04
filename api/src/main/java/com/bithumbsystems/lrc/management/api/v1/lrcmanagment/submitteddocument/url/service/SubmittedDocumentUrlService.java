package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.service;

import com.bithumbsystems.lrc.management.api.core.config.property.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.core.util.AES256Util;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.exception.SubmittedDocumentUrlException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.mapper.SubmittedDocumentUrlMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.model.request.SubmittedDocumentUrlRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.model.response.SubmittedDocumentUrlResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.url.model.entity.SubmittedDocumentUrl;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.url.service.SubmittedDocumentUrlDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmittedDocumentUrlService {

    private final SubmittedDocumentUrlDomainService submittedDocumentUrlDomainService;
    private final AwsProperties awsProperties;
    /**
     * 제출 서류 관리 id, type 으로 url 찾기
     * @param projectId
     * @return SubmittedDocumentUrlResponse Object
     */
    public Mono<List<SubmittedDocumentUrlResponse>> findByProjectId(String projectId) {
        return submittedDocumentUrlDomainService.findByProjectId(projectId)
                .flatMap(result -> {
                    return Mono.just(SubmittedDocumentUrlResponse.builder()
                            .id(result.getId())
                            .projectId(result.getProjectId())
                            .type(result.getType())
                            .url(result.getUrl())
                            .email(AES256Util.decryptAES(awsProperties.getKmsKey(), result.getEmail()))
                            .createDate(result.getCreateDate())
                            .createAdminAccountId(result.getCreateAdminAccountId())
                            .build());
                })
                //.map(SubmittedDocumentUrlMapper.INSTANCE::submittedDocumentUrlResponse)
                .switchIfEmpty(Mono.error(new SubmittedDocumentUrlException(ErrorCode.NOT_FOUND_CONTENT)))
                .collectList();
    }

    /**
     * 제출 서류 관리 url 저장
     *
     * @param submittedDocumentUrlRequest
     * @return SubmittedDocumentResponse Object
     */
    @Transactional
    public Mono<List<SubmittedDocumentUrlResponse>> saveAll(SubmittedDocumentUrlRequest submittedDocumentUrlRequest, Account account) {
        return submittedDocumentUrlDomainService.save(
                        SubmittedDocumentUrl.builder()
                                .id(UUID.randomUUID().toString())
                                .projectId(submittedDocumentUrlRequest.getProjectId())
                                .type(submittedDocumentUrlRequest.getType())
                                .url(submittedDocumentUrlRequest.getUrl())
                                .createDate(LocalDateTime.now())
                                .createAdminAccountId(account.getAccountId())
                                .email(AES256Util.encryptAES(awsProperties.getKmsKey(),account.getEmail(), false))
                                .build()
                )
                .switchIfEmpty(Mono.error(new SubmittedDocumentUrlException(ErrorCode.FAIL_CREATE_CONTENT)))
                .flatMap(res ->
                        submittedDocumentUrlDomainService.findByProjectIdAndType(res.getProjectId(), res.getType())
                                .map(SubmittedDocumentUrlMapper.INSTANCE::submittedDocumentUrlResponse)
                                .collectList()
                );
    }

    /**
     * 제출 서류 관리 url 저장
     * @param id
     * @return SubmittedDocumentResponse Object
     */
    public Mono<Void> deleteSubmittedDocumentUrl(String id) {
        return submittedDocumentUrlDomainService.findSubmittedDocumentUrlById(id)
                .flatMap(submittedDocumentUrlDomainService::deleteSubmittedDocumentUrl)
                .switchIfEmpty(Mono.error(new SubmittedDocumentUrlException(ErrorCode.FAIL_CREATE_CONTENT)));
    }
}
