package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.service;

import com.bithumbsystems.lrc.management.api.core.config.constant.RegExpConstant;
import com.bithumbsystems.lrc.management.api.core.config.properties.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.core.util.AES256Util;
import com.bithumbsystems.lrc.management.api.core.util.MaskingUtil;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.listener.HistoryDto;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.exception.SubmittedDocumentUrlException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.mapper.SubmittedDocumentUrlMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.model.request.SubmittedDocumentUrlRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.model.response.SubmittedDocumentUrlResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.service.UserAccountDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.model.enums.SubmittedDocumentEnums;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.url.model.entity.SubmittedDocumentUrl;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.url.service.SubmittedDocumentUrlDomainService;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

/**
 * The type Submitted document url service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SubmittedDocumentUrlService {
  private final SubmittedDocumentUrlDomainService submittedDocumentUrlDomainService;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final AwsProperties awsProperties;
  private final UserAccountDomainService userAccountDomainService;

  /**
   * 제출 서류 관리 id, type 으로 url 찾기.
   *
   * @param projectId the project id
   * @return SubmittedDocumentUrlResponse Object
   */
  public Mono<List<SubmittedDocumentUrlResponse>> findByProjectId(String projectId) {
    return submittedDocumentUrlDomainService.findByProjectId(projectId)
        .flatMap(result -> {
          String encryptEmail = result.getEmail();
          String decryptEmail = result.getEmail();
          if (Pattern.matches(RegExpConstant.EMAIL_REG_EXP, result.getEmail())) {
            encryptEmail = AES256Util.encryptAES(awsProperties.getKmsKey(), result.getEmail(), awsProperties.getSaltKey(), awsProperties.getIvKey());
          } else {
            decryptEmail = AES256Util.decryptAES(awsProperties.getKmsKey(), result.getEmail());
          }

          SubmittedDocumentUrlResponse submittedDocumentUrlResponse = SubmittedDocumentUrlResponse.builder()
              .id(result.getId())
              .projectId(result.getProjectId())
              .type(result.getType())
              .url(result.getUrl())
              .email(MaskingUtil.getEmailMask(decryptEmail))
              .createDate(result.getCreateDate())
              .createAdminAccountId(result.getCreateAdminAccountId())
              .createAccountId(result.getCreateAccountId())
              .build();

          return userAccountDomainService.findByProjectIdAndContactEmail(result.getProjectId(), encryptEmail)
              .flatMap(subResult -> {
                if (StringUtils.hasLength(subResult.getName())) {
                  submittedDocumentUrlResponse.setEmail(MaskingUtil.getNameMask(AES256Util.decryptAES(awsProperties.getKmsKey(), subResult.getName()))
                      + "(" + MaskingUtil.getEmailMask(AES256Util.decryptAES(awsProperties.getKmsKey(), subResult.getContactEmail())) + ")");
                }
                return Mono.just(submittedDocumentUrlResponse);
              })
              .switchIfEmpty(Mono.defer(() -> Mono.just(submittedDocumentUrlResponse)));
        })
        .collectSortedList(Comparator.comparing(SubmittedDocumentUrlResponse::getCreateDate).reversed());
  }

  /**
   * 제출 서류 관리 url 저장.
   *
   * @param submittedDocumentUrlRequest the submitted document url request
   * @param account                     the account
   * @return SubmittedDocumentResponse Object
   */
  @Transactional
  public Mono<List<SubmittedDocumentUrlResponse>> saveAll(SubmittedDocumentUrlRequest submittedDocumentUrlRequest, Account account) {
    historyLogSend(submittedDocumentUrlRequest.getProjectId(), "제출서류", submittedDocumentUrlRequest.getType(), "URL추가(관리자)",
        submittedDocumentUrlRequest.getUrl(), account);
    return submittedDocumentUrlDomainService.save(SubmittedDocumentUrl.builder()
                .id(UUID.randomUUID().toString())
                .projectId(submittedDocumentUrlRequest.getProjectId())
                .type(submittedDocumentUrlRequest.getType())
                .url(submittedDocumentUrlRequest.getUrl())
                .createDate(LocalDateTime.now())
                .createAdminAccountId(account.getAccountId())
                .email(AES256Util.encryptAES(awsProperties.getKmsKey(), account.getEmail(), awsProperties.getSaltKey(), awsProperties.getIvKey()))
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
   * 제출 서류 관리 url 저장.
   *
   * @param id the id
   * @return SubmittedDocumentResponse Object
   */
  public Mono<Void> deleteSubmittedDocumentUrl(String id) {
    return submittedDocumentUrlDomainService.findSubmittedDocumentUrlById(id)
        .flatMap(submittedDocumentUrlDomainService::deleteSubmittedDocumentUrl)
        .switchIfEmpty(Mono.error(new SubmittedDocumentUrlException(ErrorCode.FAIL_CREATE_CONTENT)));
  }

  /**
   * 변경 히스토리 저장.
   *
   * @param projectId   the project id
   * @param menu        the menu
   * @param type        the type
   * @param taskHistory the task history
   * @param item        the item
   * @param account     the account
   */
  private void historyLogSend(String projectId, String menu, SubmittedDocumentEnums type, String taskHistory, String item, Account account) {
    String subject = "";
    if (type.equals(SubmittedDocumentEnums.IPO_APPLICATION)) {
      subject = "거래지원 신청서";
    } else if (type.equals(SubmittedDocumentEnums.COLLECT_CONFIRM)) {
      subject = "개인정보 수집 및 이용동의서";
    } else if (type.equals(SubmittedDocumentEnums.PROJECT_WHITEPAPER)) {
      subject = "프로젝트 백서";
    } else if (type.equals(SubmittedDocumentEnums.TECH_REVIEW_REPORT)) {
      subject = "기술검토보고서";
    } else if (type.equals(SubmittedDocumentEnums.TOKEN_DIVISION_PLAN)) {
      subject = "토큰 세일 및 분배 계획서";
    } else if (type.equals(SubmittedDocumentEnums.LEGAL_REVIEW)) {
      subject = "법률검토의견서";
    } else if (type.equals(SubmittedDocumentEnums.BUSINESS_LICENSE)) {
      subject = "사업자등록증";
    } else if (type.equals(SubmittedDocumentEnums.ETHICAL_WRITE_AUTH)) {
      subject = "윤리서약서";
    } else if (type.equals(SubmittedDocumentEnums.REGULATORY_COMPLIANCE)) {
      subject = "규제준수 확약서";
    } else if (type.equals(SubmittedDocumentEnums.ETC)) {
      subject = "기타";
    } else if (type.equals(SubmittedDocumentEnums.SHAREHOLDER)) {
      subject = "주주명부";
    }
    applicationEventPublisher.publishEvent(HistoryDto.builder()
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
