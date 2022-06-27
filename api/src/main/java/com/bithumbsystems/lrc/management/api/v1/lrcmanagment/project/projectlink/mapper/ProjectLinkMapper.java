package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.mapper;

import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.model.request.ProjectLinkRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.model.response.ProjectLinkResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectlink.model.entity.ProjectLink;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectLinkMapper {

    ProjectLinkMapper INSTANCE = Mappers.getMapper(ProjectLinkMapper.class);

    ProjectLinkResponse projectLinkResponse(ProjectLink projectLink);

    ProjectLink projectLinkRequestToProjectLink(ProjectLinkRequest projectLinkRequest);

    ProjectLink projectLinkRequestToProjectLink(ProjectLink projectLink);
}


