package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.mail.service;

import com.bithumbsystems.lrc.management.api.core.config.properties.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.util.AES256Util;
import com.bithumbsystems.lrc.management.api.core.model.enums.MailForm;
import com.bithumbsystems.lrc.management.api.core.util.message.MessageService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.service.UserAccountDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.service.UserInfoDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class LrcMailService {
    private final MessageService messageService;
    private final UserAccountDomainService userAccountDomainService;
    private final UserInfoDomainService userInfoDomainService;
    private final AwsProperties awsProperties;
    public Mono<Boolean> sendEmail(String email, String type) {
        MailForm mailForm = type.equals("KOR") ? MailForm.KOR: MailForm.EN;
        messageService.sendMail(email, mailForm);
        return Mono.just(true);
    }

    public Mono<Boolean> sendEmailToProjectUser(String projectId, String type) {
        return userAccountDomainService.findByProjectId(projectId).flatMap(projectUser -> {
            return userInfoDomainService.findById(projectUser.getUserAccountId()).flatMap(userAccount -> {
                String email = (StringUtils.hasLength(projectUser.getContactEmail()))? AES256Util.decryptAES(awsProperties.getKmsKey(), projectUser.getContactEmail()) : AES256Util.decryptAES(awsProperties.getKmsKey(), userAccount.getEmail());
                log.debug("sendEmailToProjectUser:{}:{}", email, type);
                sendMail(email, type);
                return Mono.just(true);
            });
        }).then(Mono.just(true));
    }

}
