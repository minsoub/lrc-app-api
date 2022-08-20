package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.listener;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.model.entity.History;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.service.HistoryDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class HistoryLogListener {
    private final HistoryDomainService historyDomainService;

    @EventListener
    public void historyLog(HistoryDto historyDto) {
        log.info("historyLog event called...");
        historyDomainService.save(History.builder()
                .id(UUID.randomUUID().toString())
                .projectId(historyDto.getProjectId())
                .menu(historyDto.getMenu())
                .subject(historyDto.getSubject())
                .item(historyDto.getItem())
                .taskHistory(historyDto.getTaskHistory())
                .type("ADMIN")
                .customer(historyDto.getEmail())
                .updateAccountId(historyDto.getAccountId())
                .updateDate(LocalDateTime.now())
                .build()).publishOn(Schedulers.boundedElastic()).subscribe();
    }
}
