package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.service;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.listener.HistoryDto;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.listener.HistoryLog;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.mapper.ProjectInfoMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.model.request.ProjectInfoRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.model.response.ProjectInfoResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.model.entity.ProjectInfo;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.service.ProjectInfoDomainService;
import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.service.LineMngDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectInfoService {

    private final ProjectInfoDomainService projectInfoDomainService;
    private final LineMngDomainService lineMngDomainService;
    private final HistoryLog historyLog;
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
                            .switchIfEmpty(Mono.just(ProjectInfoResponse.builder()
                                    .id(result.getId())
                                    .projectId(result.getProjectId())
                                    .whitepaperLink(result.getWhitepaperLink())
                                    .businessCode(result.getBusinessCode())
                                    .businessName("")
                                    .networkCode(result.getNetworkCode())
                                    .createDate(result.getCreateDate())
                                    .contractAddress(result.getContractAddress())
                                    .build()));

                })
                .flatMap(res -> {
                                return lineMngDomainService.findById(res.getNetworkCode())
                                        .flatMap(c -> {
                                            res.setNetworkName(c.getName());
                                            return Mono.just(res);
                                        })
                                        .switchIfEmpty(Mono.just(res));
                });
    }

    /**
     * 프로젝트 정보 1개 저장
     * @param projectInfoRequest
     * @return ProjectInfoResponse Object
     */
    public Mono<ProjectInfoResponse> create(ProjectInfoRequest projectInfoRequest) {
        return projectInfoDomainService.save(ProjectInfoMapper.INSTANCE.projectInfoRequestToProjectInfo(projectInfoRequest))
                .map(ProjectInfoMapper.INSTANCE::projectInfoResponse);
                //.switchIfEmpty(Mono.error(new ProjectInfoException(ErrorCode.FAIL_CREATE_CONTENT)));
    }

    /**
     * 프로젝트 정보 업데이트
     * @param projectInfoRequest
     * @return ProjectInfoResponse Object
     */
    public Mono<ProjectInfoResponse> updateProjectInfo(String projectId, ProjectInfoRequest projectInfoRequest, Account account) {

        return projectInfoDomainService.findByProjectId(projectId)
                .flatMap(c -> {
                    // 변경 히스토리 추가
                    if ((c.getBusinessCode() == null && projectInfoRequest.getBusinessCode() != null) || !c.getBusinessCode().equals(projectInfoRequest.getBusinessCode())) {
                        historyLog.send(projectId, "프로젝트 관리>프로젝트 정보", "사업 계열", "수정", projectInfoRequest.getBusinessCode(), account);
                    }
                    if ((c.getNetworkCode() == null && projectInfoRequest.getNetworkCode() != null) || !c.getNetworkCode().equals(projectInfoRequest.getNetworkCode())) {
                        historyLog.send(projectId, "프로젝트 관리>프로젝트 정보", "네트워크 계열", "수정", projectInfoRequest.getNetworkCode(), account);
                    }
                    if ((c.getWhitepaperLink() == null && projectInfoRequest.getWhitepaperLink() != null) || !c.getWhitepaperLink().equals(projectInfoRequest.getWhitepaperLink())) {
                        historyLog.send(projectId, "프로젝트 관리>프로젝트 정보", "Jira 번호", "수정", projectInfoRequest.getWhitepaperLink(), account);
                    }
                    if (c.getCreateDate() == null && projectInfoRequest.getCreateDate() == null) {
                        // no job
                    } else if ((c.getCreateDate() == null && projectInfoRequest.getCreateDate() != null) || !c.getCreateDate().equals(projectInfoRequest.getCreateDate())) {
                        historyLog.send(projectId, "프로젝트 관리>프로젝트 정보", "최초 발행일", "수정", projectInfoRequest.getCreateDate() == null ? "" : projectInfoRequest.getCreateDate().toString(), account);
                    }
                    if ((c.getContractAddress() == null && projectInfoRequest.getContractAddress() != null) || !c.getContractAddress().equals(projectInfoRequest.getContractAddress())) {
                        historyLog.send(projectId, "프로젝트 관리>프로젝트 정보", "컨트렉트 주소", "수정", projectInfoRequest.getContractAddress(), account);
                    }
                    c.setBusinessCode(projectInfoRequest.getBusinessCode());
                    c.setNetworkCode(projectInfoRequest.getNetworkCode());
                    c.setWhitepaperLink(projectInfoRequest.getWhitepaperLink());
                    c.setContractAddress(projectInfoRequest.getContractAddress());
                    c.setCreateDate(projectInfoRequest.getCreateDate());
                    return projectInfoDomainService.updateProjectInfo(c)
                            .map(ProjectInfoMapper.INSTANCE::projectInfoResponse);
                })
                .switchIfEmpty(Mono.defer(() -> {
                        if (StringUtils.hasLength(projectInfoRequest.getBusinessCode())) {
                            historyLog.send(projectId, "프로젝트 관리>프로젝트 정보", "사업 계열", "등록", projectInfoRequest.getBusinessCode(), account);
                        }
                        if (StringUtils.hasLength(projectInfoRequest.getNetworkCode())) {
                            historyLog.send(projectId, "프로젝트 관리>프로젝트 정보", "네트워크 계열", "등록", projectInfoRequest.getNetworkCode(), account);
                        }
                        if (StringUtils.hasLength(projectInfoRequest.getWhitepaperLink())) {
                            historyLog.send(projectId, "프로젝트 관리>프로젝트 정보", "Jira 번호", "등록", projectInfoRequest.getWhitepaperLink(), account);
                        }
                        if (StringUtils.hasLength(projectInfoRequest.getContractAddress())) {
                            historyLog.send(projectId, "프로젝트 관리>프로젝트 정보", "최초 발행일", "등록", projectInfoRequest.getContractAddress(), account);
                        }
                        if (!StringUtils.isEmpty(projectInfoRequest.getCreateDate())) {
                            historyLog.send(projectId, "프로젝트 관리>프로젝트 정보", "컨트렉트 주소", "등록", projectInfoRequest.getCreateDate().toString(), account);
                        }
                       return  projectInfoDomainService.save(ProjectInfo.builder()
                                        .id(UUID.randomUUID().toString())
                                        .projectId(projectInfoRequest.getProjectId())
                                        .networkCode(projectInfoRequest.getNetworkCode())
                                        .businessCode(projectInfoRequest.getBusinessCode())
                                        .whitepaperLink(projectInfoRequest.getWhitepaperLink())
                                        .contractAddress(projectInfoRequest.getContractAddress())
                                        .createDate(projectInfoRequest.getCreateDate())
                                        .build())
                                .map(ProjectInfoMapper.INSTANCE::projectInfoResponse);

                    })
                );
    }
}
