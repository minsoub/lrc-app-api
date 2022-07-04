package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.service;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
<<<<<<< HEAD
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.listener.HistoryDto;
=======
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.exception.ProjectInfoException;
>>>>>>> e9f49e4e4f3c990b843c34a51966534586594904
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.mapper.ProjectInfoMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.model.request.ProjectInfoRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.model.response.ProjectInfoResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.model.entity.History;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.service.HistoryDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.service.ProjectInfoDomainService;
import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.service.LineMngDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectInfoService {

    private final ProjectInfoDomainService projectInfoDomainService;
    private final LineMngDomainService lineMngDomainService;
    private final ApplicationEventPublisher applicationEventPublisher;
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
                .switchIfEmpty(Mono.error(new ProjectInfoException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 프로젝트 정보 1개 저장
     * @param projectInfoRequest
     * @return ProjectInfoResponse Object
     */
    public Mono<ProjectInfoResponse> create(ProjectInfoRequest projectInfoRequest) {
        return projectInfoDomainService.save(ProjectInfoMapper.INSTANCE.projectInfoRequestToProjectInfo(projectInfoRequest))
                .map(ProjectInfoMapper.INSTANCE::projectInfoResponse)
                .switchIfEmpty(Mono.error(new ProjectInfoException(ErrorCode.FAIL_CREATE_CONTENT)));
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
                    if (!c.getBusinessCode().equals(projectInfoRequest.getBusinessCode())) {
                        historyLogSend(projectId, "프로젝트 관리>프로젝트 정보", "사업 계열", "수정", account);
                    }
                    if (!c.getNetworkCode().equals(projectInfoRequest.getNetworkCode())) {
                        historyLogSend(projectId, "프로젝트 관리>프로젝트 정보", "네트워크 계열", "수정", account);
                    }
                    if (!c.getWhitepaperLink().equals(projectInfoRequest.getWhitepaperLink())) {
                        historyLogSend(projectId, "프로젝트 관리>프로젝트 정보", "백서 링크", "수정", account);
                    }
                    if (!c.getCreateDate().equals(projectInfoRequest.getCreateDate())) {
                        historyLogSend(projectId, "프로젝트 관리>프로젝트 정보", "최초 발행일", "수정", account);
                    }
                    if (!c.getContractAddress().equals(projectInfoRequest.getContractAddress())) {
                        historyLogSend(projectId, "프로젝트 관리>프로젝트 정보", "컨트렉트 주소", "수정", account);
                    }
                    c.setBusinessCode(projectInfoRequest.getBusinessCode());
                    c.setNetworkCode(projectInfoRequest.getNetworkCode());
                    c.setWhitepaperLink(projectInfoRequest.getWhitepaperLink());
                    c.setContractAddress(projectInfoRequest.getContractAddress());
                    c.setCreateDate(projectInfoRequest.getCreateDate());
                    return projectInfoDomainService.updateProjectInfo(c)
                            .map(ProjectInfoMapper.INSTANCE::projectInfoResponse);
                })
                .switchIfEmpty(Mono.error(new ProjectInfoException(ErrorCode.FAIL_UPDATE_CONTENT)));
    }

    /**
     * 변경 히스토리 저장.
     *
     * @param projectId
     * @param menu
     * @param subject
     * @param taskHistory
     * @param account
     * @return
     */
    private void historyLogSend(String projectId, String menu, String subject, String taskHistory, Account account) {
        applicationEventPublisher.publishEvent(
                HistoryDto.builder()
                        .projectId(projectId)
                        .menu(menu)
                        .subject(subject)
                        .taskHistory(taskHistory)
                        .email(account.getEmail())
                        .accountId(account.getAccountId())
                        .build()
        );
    }
}
