package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.mail.service;

import com.bithumbsystems.lrc.management.api.core.model.enums.MailForm;
import com.bithumbsystems.lrc.management.api.core.util.message.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class LrcMailService {
    private final MessageService messageService;
    public Mono<Boolean> sendEmail(String email, String type) {
        MailForm mailForm = type.equals("KOR") ? MailForm.KOR: MailForm.EN;
        messageService.sendMail(email, mailForm);
        return Mono.just(true);
    }
}
