package com.bithumbsystems.lrc.management.api.v1.audit.mapper;

import com.bithumbsystems.lrc.management.api.v1.audit.model.response.AuditLogDetailResponse;
import com.bithumbsystems.lrc.management.api.v1.audit.model.response.AuditLogResponse;
import com.bithumbsystems.persistence.mongodb.audit.model.entity.AuditLog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuditLogMapper {

    AuditLogMapper INSTANCE = Mappers.getMapper(AuditLogMapper.class);

    AuditLogResponse auditLogResponse(AuditLog auditLog);

    AuditLogDetailResponse auditLogDetailResponse(AuditLog auditLog);
}
