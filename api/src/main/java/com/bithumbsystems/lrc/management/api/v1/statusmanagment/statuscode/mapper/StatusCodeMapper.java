package com.bithumbsystems.lrc.management.api.v1.statusmanagment.statuscode.mapper;

import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statuscode.model.request.StatusCodeRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statuscode.model.request.StatusModifyRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statuscode.model.response.StatusCodeResponse;
import com.bithumbsystems.persistence.mongodb.statusmanagement.statuscode.model.entity.StatusCode;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StatusCodeMapper {

    StatusCodeMapper INSTANCE = Mappers.getMapper(StatusCodeMapper.class);

    StatusCodeResponse statusCodeResponse(StatusCode statusCode);

    StatusCode statusCodeRequestToStatusCode(StatusCodeRequest statusCodeRequest);

    StatusCode statusCodeModifyRequestToStatusCode(StatusModifyRequest statusModifyRequest);


}
