package com.bithumbsystems.lrc.management.api.v1.audit.service;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.audit.exception.AuditLogException;
import com.bithumbsystems.lrc.management.api.v1.audit.mapper.AuditLogMapper;
import com.bithumbsystems.lrc.management.api.v1.audit.model.response.AuditLogResponse;
import com.bithumbsystems.persistence.mongodb.audit.service.AuditLogDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogDomainService auditLogDomainService;

    public Mono<List<AuditLogResponse>> findAuditServiceLog(LocalDateTime fromDate, LocalDateTime toDate, String keyword, String mySiteId) {

        return auditLogDomainService.findPageBySearchText(
                        fromDate,
                        toDate, keyword, mySiteId)
                .map(AuditLogMapper.INSTANCE::auditLogResponse)
                .collectSortedList(Comparator.comparing(AuditLogResponse::getCreateDate))
                .switchIfEmpty(Mono.error(new AuditLogException(ErrorCode.NOT_FOUND_CONTENT)));
    }
}
