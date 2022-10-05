package com.bithumbsystems.lrc.management.api.v1.accesslog.listener;

import com.bithumbsystems.lrc.management.api.v1.accesslog.request.AccessLogRequest;
import com.bithumbsystems.persistence.mongodb.accesslog.model.entity.AccessLog;
import com.bithumbsystems.persistence.mongodb.accesslog.service.AccessLogDomainService;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccessLogListener {
    private final AccessLogDomainService accessLogDomainService;

    @EventListener
    public void accessLog(AccessLogRequest accessLogRequest) {
        accessLogDomainService.insert(AccessLog.builder()
                .id(UUID.randomUUID().toString())
                .accountId(accessLogRequest.getAccountId())
                .email(accessLogRequest.getEmail())
                .actionType(accessLogRequest.getActionType())
                .reason(accessLogRequest.getReason())
                .description(accessLogRequest.getDescription())
                .ip(accessLogRequest.getIp())
                .siteId(accessLogRequest.getSiteId())
                .createDate(LocalDateTime.now())
                .build()).publishOn(Schedulers.single()).subscribe();
    }
}
