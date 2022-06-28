package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.service;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.model.request.ProjectInfoRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.model.response.ProjectInfoResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.mapper.ProejctInfoMapper;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.service.ProjectInfoDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProjectInfoService {

    private final ProjectInfoDomainService projectInfoDomainService;

    /**
     * 프로젝트 정보 1개 id 찾기
     * @param projectId
     * @return ProjectInfoResponse Object
     */
    public Mono<ProjectInfoResponse> findByProjectId(String projectId) {
        return projectInfoDomainService.findByProjectId(projectId)
                .map(ProejctInfoMapper.INSTANCE::projectInfoResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));

    }

    /**
     * 프로젝트 정보 1개 저장
     * @param projectInfoRequest
     * @return ProjectInfoResponse Object
     */
    public Mono<ProjectInfoResponse> create(ProjectInfoRequest projectInfoRequest) {
        return projectInfoDomainService.save(ProejctInfoMapper.INSTANCE.projectInfoRequestToProjectInfo(projectInfoRequest))
                .map(ProejctInfoMapper.INSTANCE::projectInfoResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 프로젝트 정보 업데이트
     * @param projectInfoRequest
     * @return ProjectInfoResponse Object
     */
    public Mono<ProjectInfoResponse> updateProjectInfo(String projectId, ProjectInfoRequest projectInfoRequest) {
        return projectInfoDomainService.findByProjectId(projectId)
                .flatMap(c -> {
                    c.setBusinessCode(projectInfoRequest.getBusinessCode());
                    c.setNetworkCode(projectInfoRequest.getNetworkCode());
                    c.setWhitepaperLink(projectInfoRequest.getWhitepaperLink());
                    c.setContractAddress(projectInfoRequest.getContractAddress());
                    return projectInfoDomainService.updateProjectInfo(c)
                            .map(ProejctInfoMapper.INSTANCE::projectInfoResponse);
                })
        .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.FAIL_UPDATE_CONTENT)));
    }
}
