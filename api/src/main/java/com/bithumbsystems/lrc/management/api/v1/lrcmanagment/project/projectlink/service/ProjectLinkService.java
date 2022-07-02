package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.service;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.mapper.ProjectLinkMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.model.request.ProjectLinkRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.model.response.FoundationLinkResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.model.response.ProjectLinkResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.foundation.service.FoundationDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.service.FoundationInfoDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectlink.model.entity.ProjectLink;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectlink.service.ProjectLinkDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectLinkService {

    private final ProjectLinkDomainService projectLinkDomainService;

    private final FoundationInfoDomainService foundationDomainService;

    /**
     * 프로젝트 링크 가져오기
     * @param projectId
     * @return FoundationResponse
     */
    public Mono<List<ProjectLinkResponse>> findByProjectLinkList(String projectId) {
        return projectLinkDomainService.findByProjectLinkList(projectId)
                .flatMap(result -> {
                    return foundationDomainService.findById(result.getLinkProjectId())
                            .flatMap(res -> {
                                return  Mono.just(ProjectLinkResponse.builder()
                                                .id(result.getId())
                                                .projectId(result.getProjectId())
                                                .symbol(result.getSymbol())
                                                .linkProjectId(result.getLinkProjectId())
                                                .linkProjectName(res.getName())
                                                .linkProjectSymbol(result.getLinkProjectSymbol())
                                                .build());
                            });
                    //ProjectLinkMapper.INSTANCE::projectLinkResponse
                })
                .collectList()
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 프로젝트 연결 재단 조회
     * @param symbol
     * @return FoundationResponse
     */
    public Mono<List<FoundationLinkResponse>> findBySymbolLike(String symbol, String projectId) {
        return foundationDomainService.findBySymbolSearch(symbol)
                .filter(f -> !f.getId().equals(projectId))  // 내 프로젝트 제외
                .map(c -> {
                    FoundationLinkResponse foundationLinkResponse = FoundationLinkResponse.builder()
                            .id(c.getId())
                            .projectId(c.getId())
                            .projectName(c.getName())
                            .symbol(c.getSymbol())
                            .build();
                    return foundationLinkResponse;
                })
                .collectList()
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 프로젝트 링크 파라미터 가져오기
     * @param projectId
     * @param linkProjectId
     * @return FoundationResponse
     */
    public Mono<ProjectLinkResponse> findByLinkProject(String projectId, String linkProjectId) {
        return projectLinkDomainService.findByLinkProject(projectId, linkProjectId)
                .map(ProjectLinkMapper.INSTANCE::projectLinkResponse)
                //.collectList()
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 프로젝트 링크 저장
     * @param projectLinkRequest
     * @return FoundationResponse
     */
    public Mono<List<ProjectLinkResponse>> create(ProjectLinkRequest projectLinkRequest) {
        return projectLinkDomainService.save(ProjectLinkMapper.INSTANCE.projectLinkRequestToProjectLink(projectLinkRequest))
                .flatMap(projectLink -> {
                    ProjectLinkRequest projectLinkRequest1 = new ProjectLinkRequest();
                    projectLinkRequest1.setProjectId(projectLink.getLinkProjectId());
                    projectLinkRequest1.setSymbol(projectLink.getLinkProjectSymbol());
                    projectLinkRequest1.setLinkProjectId(projectLink.getProjectId());
                    projectLinkRequest1.setLinkProjectSymbol(projectLinkRequest.getSymbol());

                    return projectLinkDomainService.save(ProjectLinkMapper.INSTANCE.projectLinkRequestToProjectLink(projectLinkRequest1));
                })
                .then(this.findByProjectLinkList(projectLinkRequest.getProjectId()))
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 프로젝트 링크 삭제(링크해제)
     *
     * @param linkId
     * @return FoundationResponse
     */
    public Mono<ProjectLinkResponse> deleteLinkProject(String linkId) {
        return projectLinkDomainService.findById(linkId)
                .flatMap(projectLink ->projectLinkDomainService.deleteLinkProject(projectLink))
                .map(r1 -> {
                    return projectLinkDomainService.findByLinkProject(r1.getLinkProjectId(), r1.getProjectId())
                            .flatMap(r2 -> projectLinkDomainService.deleteLinkProject(r2));
                })
                .flatMap(r2 -> r2.map(r -> {
                    return ProjectLinkResponse.builder()
                            .id(r.getId())
                            .build();
                }));
    }
}
