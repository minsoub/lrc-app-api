package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectlink.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectlink.model.entity.ProjectLink;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectlink.repository.ProjectLinkCustomRepository;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectlink.repository.ProjectLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProjectLinkDomainService {

    private final ProjectLinkRepository projectLinkRepository;

    private final ProjectLinkCustomRepository projectLinkCustomRepository;


    /**
     * 프로젝트 링크 true 인것 가져오기
     * @param projectId
     * @return FoundationResponse
     */
    public Flux<ProjectLink> findByProjectLinkList(String projectId) {
        return projectLinkCustomRepository.findByProjectLinkList(projectId);
    }

    /**
     * 프로젝트 링크 파라미터 true 가져오기
     * @param projectId
     * @param linkProjectId
     * @return FoundationResponse
     */
    public Flux<ProjectLink> findByLinkProject(String projectId, String linkProjectId) {
        return projectLinkCustomRepository.findByLinkProject(projectId, linkProjectId);
    }

    /**
     * 프로젝트 링크 생성
     * @param projectLink
     * @return FoundationResponse
     */
    public Mono<ProjectLink> save(ProjectLink projectLink) {
        projectLink.setUseYn(true); //true 인것만
        return projectLinkRepository.insert(projectLink);
    }

    /**
     * 프로젝트 링크 false 저장(링크해제)
     * @param projectLink
     * @return FoundationResponse
     */
    public Mono<ProjectLink> deleteLinkProject(ProjectLink projectLink) {
        projectLink.setUseYn(false); //false
        return projectLinkRepository.save(projectLink);
    }
}
