package com.bithumbsystems.persistence.mongodb.audit.service;

import com.bithumbsystems.persistence.mongodb.audit.model.entity.AuditLog;
import com.bithumbsystems.persistence.mongodb.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogDomainService {

    private final AuditLogRepository auditLogRepository;

    public Flux<AuditLog> findPageBySearchText(LocalDateTime fromDate, LocalDateTime toDate, String keyword, String mySiteId) {
      return auditLogRepository.findPageBySearchText(fromDate, toDate, keyword, mySiteId);
    }
}
