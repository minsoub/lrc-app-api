package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.service;

import com.bithumbsystems.lrc.management.api.core.config.property.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.core.util.AES256Util;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.listener.HistoryDto;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.model.response.SubmittedDocumentFileResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.mapper.SubmittedDocumentUrlMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.model.request.SubmittedDocumentUrlRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.model.response.SubmittedDocumentUrlResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.model.entity.History;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.service.HistoryDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.model.enums.SubmittedDocumentEnums;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.url.model.entity.SubmittedDocumentUrl;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.url.service.SubmittedDocumentUrlDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher applicationEventPublisher;
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
                            .createAccountId(result.getCreateAccountId())
                            .build());
                })
                //.map(SubmittedDocumentUrlMapper.INSTANCE::submittedDocumentUrlResponse)
                .collectList()
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 제출 서류 관리 url 저장
     *
     * @param submittedDocumentUrlRequest
     * @return SubmittedDocumentResponse Object
     */
    @Transactional
    public Mono<List<SubmittedDocumentUrlResponse>> saveAll(SubmittedDocumentUrlRequest submittedDocumentUrlRequest, Account account) {
        historyLogSend(submittedDocumentUrlRequest.getProjectId(), "제출서류", submittedDocumentUrlRequest.getType(), "URL추가(관리자)", account);
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
                .flatMap(res ->
                        submittedDocumentUrlDomainService.findByProjectIdAndType(res.getProjectId(), res.getType())
                                .map(SubmittedDocumentUrlMapper.INSTANCE::submittedDocumentUrlResponse)
                                .collectList()
                )
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 제출 서류 관리 url 저장
     * @param id
     * @return SubmittedDocumentResponse Object
     */
    public Mono<Void> deleteSubmittedDocumentUrl(String id) {
        return submittedDocumentUrlDomainService.findSubmittedDocumentUrlById(id)
                .flatMap(submittedDocumentUrlDomainService::deleteSubmittedDocumentUrl);
    }

    /**
     * 변경 히스토리 저장.
     *
     * @param projectId
     * @param menu
     * @param type
     * @param taskHistory
     * @param account
     * @return
     */
    private void historyLogSend(String projectId, String menu, SubmittedDocumentEnums type, String taskHistory, Account account) {
        String subject = "";
        if (type.equals(SubmittedDocumentEnums.IPO_APPLICATION)) {
            subject = "거래지원 신청서";
        } else if(type.equals(SubmittedDocumentEnums.COLLECT_CONFIRM)) {
            subject = "개인정보 수집 및 이용동의서";
        } else if(type.equals(SubmittedDocumentEnums.PROJECT_WHITEPAPER)) {
            subject = "프로젝트 백서";
        } else if(type.equals(SubmittedDocumentEnums.TECH_REVIEW_REPORT)) {
            subject = "기술검토보고서";
        } else if(type.equals(SubmittedDocumentEnums.TOKEN_DIVISION_PLAN)) {
            subject = "토큰 세일 및 분배 계획서";
        } else if(type.equals(SubmittedDocumentEnums.LEGAL_REVIEW)) {
            subject = "법률검토의견서";
        } else if(type.equals(SubmittedDocumentEnums.BUSINESS_LICENSE)) {
            subject = "사업자등록증";
        } else if(type.equals(SubmittedDocumentEnums.ETHICAL_WRITE_AUTH)) {
            subject = "윤리서약서";
        } else if(type.equals(SubmittedDocumentEnums.REGULATORY_COMPLIANCE)) {
            subject = "규제준수 확약서";
        } else if(type.equals(SubmittedDocumentEnums.ETC)) {
            subject = "기타";
        }
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
