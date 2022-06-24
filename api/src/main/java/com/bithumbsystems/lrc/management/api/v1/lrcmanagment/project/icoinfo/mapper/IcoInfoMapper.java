package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.mapper;

import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.model.request.IcoInfoRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.model.response.IcoInfoResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.icoinfo.model.entity.IcoInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IcoInfoMapper {

    IcoInfoMapper INSTANCE = Mappers.getMapper(IcoInfoMapper.class);

    IcoInfoResponse icoInfoResponse(IcoInfo icoInfo);

    IcoInfo icoInfoRequestToIcoInfo(IcoInfoRequest.Ico icoInfoRequest);
}
