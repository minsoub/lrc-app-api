package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectlink.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectlink.model.entity.ProjectLink;
import reactor.core.publisher.Flux;

public interface ProjectLinkCustomRepository {

    /**
     * 프로젝트 링크 파라미터 true 가져오기
     * @param projectId
     * @return FoundationResponse
     */
    public Flux<ProjectLink> findByProjectLinkList(String projectId);

    /**
     * 프로젝트 링크 파라미터 true 가져오기
     * @param projectId
     * @param linkProjectId
     * @return FoundationResponse
     */
    public Flux<ProjectLink> findByLinkProject(String projectId, String linkProjectId);
}
