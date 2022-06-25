package com.bithumbsystems.lrc.management.api.v1.statusmanagment.linemng.mapper;

import com.bithumbsystems.lrc.management.api.v1.statusmanagment.linemng.model.request.LineMngRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.linemng.model.response.LineMngResponse;
import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.model.entity.LineMng;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LineMngMapper {

    LineMngMapper INSTANCE = Mappers.getMapper(LineMngMapper.class);

    LineMngResponse businessListResponse(LineMng businessList);

    LineMng businessListRequestToBusinessList(LineMngRequest businessListRequest);
}
