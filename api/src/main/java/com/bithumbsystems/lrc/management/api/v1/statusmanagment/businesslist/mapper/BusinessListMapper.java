package com.bithumbsystems.lrc.management.api.v1.statusmanagment.businesslist.mapper;

import com.bithumbsystems.lrc.management.api.v1.statusmanagment.businesslist.model.request.BusinessListRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.businesslist.model.response.BusinessListResponse;
import com.bithumbsystems.persistence.mongodb.statusmanagement.businesslist.model.entity.BusinessList;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BusinessListMapper {

    BusinessListMapper INSTANCE = Mappers.getMapper(BusinessListMapper.class);

    BusinessListResponse businessListResponse(BusinessList businessList);

    BusinessList businessListRequestToBusinessList(BusinessListRequest businessListRequest);
}
