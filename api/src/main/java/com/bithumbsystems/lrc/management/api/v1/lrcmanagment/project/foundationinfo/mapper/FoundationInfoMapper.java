package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.mapper;

import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.model.request.FoundationInfoRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.model.response.FoundationInfoResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.model.entity.FoundationInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FoundationInfoMapper {

    FoundationInfoMapper INSTANCE = Mappers.getMapper(FoundationInfoMapper.class);

    FoundationInfoResponse foundationInfoResponse(FoundationInfo foundationInfo);

    FoundationInfo foundationInfoRequestToFoundationInfo(FoundationInfoRequest foundationInfoRequest);
}
