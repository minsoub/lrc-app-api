package com.bithumbsystems.lrc.management.api.v1.statusmanagment.networklist.mapper;

import com.bithumbsystems.lrc.management.api.v1.statusmanagment.networklist.model.request.NetworkListRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.networklist.model.response.NetworkListResponse;
import com.bithumbsystems.persistence.mongodb.statusmanagement.networklist.model.entity.NetworkList;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NetworkListMapper {

    NetworkListMapper INSTANCE = Mappers.getMapper(NetworkListMapper.class);

    NetworkListResponse networkListResponse(NetworkList networkList);

    NetworkList networkListRequestToNetworkList(NetworkListRequest networkListRequest);
}
