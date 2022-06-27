package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.service;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.mapper.ProjectLinkMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.model.request.ProjectLinkRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.model.response.FoundationLinkResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.model.response.ProjectLinkResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.foundation.service.FoundationDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectlink.service.ProjectLinkDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectLinkService {

    private final ProjectLinkDomainService projectLinkDomainService;

    private final FoundationDomainService foundationDomainService;

    /**
     * 프로젝트 링크 가져오기
     * @param projectId
     * @return FoundationResponse
     */
    public Mono<List<ProjectLinkResponse>> findByProjectLinkList(String projectId) {
        return projectLinkDomainService.findByProjectLinkList(projectId)
                .map(ProjectLinkMapper.INSTANCE::projectLinkResponse)
                .collectList()
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 프로젝트 연결 재단 조회
     * @param symbol
     * @return FoundationResponse
     */
    public Mono<List<FoundationLinkResponse>> findBySymbolLike(String symbol) {
        return foundationDomainService.findBySymbolLikeIgnoreCase(symbol)
                .map(c -> {
                    FoundationLinkResponse foundationLinkResponse = FoundationLinkResponse.builder()
                            .id(c.getId())
                            .projectId(c.getProjectId())
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
    public Mono<List<ProjectLinkResponse>> findByLinkProject(String projectId, String linkProjectId) {
        return projectLinkDomainService.findByLinkProject(projectId, linkProjectId)
                .map(ProjectLinkMapper.INSTANCE::projectLinkResponse)
                .collectList()
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 프로젝트 링크 저장
     * @param projectLinkRequest
     * @return FoundationResponse
     */
    public Mono<List<ProjectLinkResponse>> create(ProjectLinkRequest projectLinkRequest) {
        return projectLinkDomainService.save(ProjectLinkMapper.INSTANCE.projectLinkRequestToProjectLink(projectLinkRequest))
                .publishOn(Schedulers.boundedElastic())
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
     * @param projectLinkRequest
     * @return FoundationResponse
     */
    public Flux<ProjectLinkResponse> deleteLinkProject(ProjectLinkRequest projectLinkRequest) {
        return projectLinkDomainService.findByLinkProject(projectLinkRequest.getProjectId(), projectLinkRequest.getLinkProjectId())
                .flatMap(projectLink ->
                        projectLinkDomainService.deleteLinkProject(ProjectLinkMapper.INSTANCE.projectLinkRequestToProjectLink(projectLink))
                )
                .publishOn(Schedulers.boundedElastic())
                .flatMap(projectLink1 ->
                        projectLinkDomainService.findByLinkProject(projectLink1.getLinkProjectId(), projectLink1.getProjectId())
                                .flatMap(projectLink2 ->
                                        projectLinkDomainService.deleteLinkProject(ProjectLinkMapper.INSTANCE.projectLinkRequestToProjectLink(projectLink2))
                                                .map(ProjectLinkMapper.INSTANCE::projectLinkResponse)
                                )
                )
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }
}
