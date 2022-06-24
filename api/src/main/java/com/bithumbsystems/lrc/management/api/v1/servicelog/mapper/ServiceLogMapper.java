package com.bithumbsystems.lrc.management.api.v1.servicelog.mapper;

import com.bithumbsystems.lrc.management.api.v1.servicelog.model.request.ServiceLogRequest;
import com.bithumbsystems.lrc.management.api.v1.servicelog.model.response.ServiceLogResponse;
import com.bithumbsystems.persistence.mongodb.servicelog.model.entity.ServiceLog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ServiceLogMapper {

    ServiceLogMapper INSTANCE = Mappers.getMapper(ServiceLogMapper.class);

    ServiceLogResponse serviceLogResponse(ServiceLog serviceLog);

    ServiceLog serviceLogRequestToServiceLog(ServiceLogRequest serviceLogRequest);
}
