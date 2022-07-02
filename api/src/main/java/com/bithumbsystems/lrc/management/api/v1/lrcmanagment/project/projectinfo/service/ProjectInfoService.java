package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.service;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.mapper.ProjectInfoMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.model.request.ProjectInfoRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.model.response.ProjectInfoResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.service.ProjectInfoDomainService;
import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.service.LineMngDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProjectInfoService {

    private final ProjectInfoDomainService projectInfoDomainService;
    private final LineMngDomainService lineMngDomainService;

    /**
     * 프로젝트 정보 1개 id 찾기
     * @param projectId
     * @return ProjectInfoResponse Object
     */
    public Mono<ProjectInfoResponse> findByProjectId(String projectId) {
        return projectInfoDomainService.findByProjectId(projectId)
                .flatMap(result -> {
                    return lineMngDomainService.findById(result.getBusinessCode())
                            .flatMap(r -> {
                                return Mono.just(ProjectInfoResponse.builder()
                                        .id(result.getId())
                                        .projectId(result.getProjectId())
                                        .whitepaperLink(result.getWhitepaperLink())
                                        .businessCode(result.getBusinessCode())
                                        .businessName(r.getName())
                                        .networkCode(result.getNetworkCode())
                                        .createDate(result.getCreateDate())
                                        .contractAddress(result.getContractAddress())
                                        .build());
                            })
                            .flatMap(res -> {
                                return lineMngDomainService.findById(res.getNetworkCode())
                                        .flatMap(c -> {
                                            res.setNetworkName(c.getName());
                                            return Mono.just(res);
                                        });
                            });
                })
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 프로젝트 정보 1개 저장
     * @param projectInfoRequest
     * @return ProjectInfoResponse Object
     */
    public Mono<ProjectInfoResponse> create(ProjectInfoRequest projectInfoRequest) {
        return projectInfoDomainService.save(ProjectInfoMapper.INSTANCE.projectInfoRequestToProjectInfo(projectInfoRequest))
                .map(ProjectInfoMapper.INSTANCE::projectInfoResponse)
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
                    c.setCreateDate(projectInfoRequest.getCreateDate());
                    return projectInfoDomainService.updateProjectInfo(c)
                            .map(ProjectInfoMapper.INSTANCE::projectInfoResponse);
                })
        .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.FAIL_UPDATE_CONTENT)));
    }
}
