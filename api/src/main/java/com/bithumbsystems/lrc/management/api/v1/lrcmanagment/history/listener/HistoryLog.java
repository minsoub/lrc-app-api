package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.listener;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;

/**
 * History Log 데이터를 저장하는 클래스
 */
@Configuration
@RequiredArgsConstructor
public class HistoryLog {

    private final ApplicationEventPublisher applicationEventPublisher;
    /**
     * 변경 히스토리 저장.
     *
     * @param projectId
     * @param menu
     * @param subject
     * @param taskHistory
     * @param account
     * @return
     */
    public void send(String projectId, String menu, String subject, String taskHistory, String item, Account account) {
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
