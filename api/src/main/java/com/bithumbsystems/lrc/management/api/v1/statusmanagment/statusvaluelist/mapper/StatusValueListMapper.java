package com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.mapper;

import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.model.request.StatusValueListRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.model.response.StatusValueListResponse;
import com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.model.entity.StatusValueList;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StatusValueListMapper {

    StatusValueListMapper INSTANCE = Mappers.getMapper(StatusValueListMapper.class);

    StatusValueListResponse statusValueListResponse(StatusValueList statusValueList);

    StatusValueList statusValueRequestToStatusValueList(StatusValueListRequest statusValueListRequest);
}
