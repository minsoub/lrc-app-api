package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.mapper;

import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.model.request.ProjectInfoRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.model.response.ProjectInfoResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.model.entity.ProjectInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProejctInfoMapper {

    ProejctInfoMapper INSTANCE = Mappers.getMapper(ProejctInfoMapper.class);

    ProjectInfoResponse projectInfoResponse(ProjectInfo projectInfo);

    ProjectInfo projectInfoRequestToProjectInfo(ProjectInfoRequest projectInfoRequest);

}
