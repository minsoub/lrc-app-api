package com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.mapper;

import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.model.request.StatusCodeRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.model.request.StatusModifyRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.model.response.StatusCodeResponse;
import com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.model.entity.StatusCode;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StatusValueListMapper {

    StatusValueListMapper INSTANCE = Mappers.getMapper(StatusValueListMapper.class);

    StatusCodeResponse statusValueListResponse(StatusCode lrcStatusCode);

    StatusCode statusCodeRequestToStatusValueList(StatusCodeRequest statusValueListRequest);

    StatusCode statusCodeModifyRequestToStatusValueList(StatusModifyRequest statusModifyRequest);


}
