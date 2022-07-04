package com.bithumbsystems.persistence.mongodb.audit.repository;

import com.bithumbsystems.persistence.mongodb.audit.model.entity.AuditLog;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AuditLogCustomRepository {

  Flux<AuditLog> findPageBySearchText(LocalDate fromDate, LocalDate toDate, String keyword, String mySiteId);
}
