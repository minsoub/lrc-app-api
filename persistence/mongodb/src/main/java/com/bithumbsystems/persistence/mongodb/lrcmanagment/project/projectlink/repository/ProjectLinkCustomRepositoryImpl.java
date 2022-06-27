package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectlink.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectlink.model.entity.ProjectLink;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProjectLinkCustomRepositoryImpl implements ProjectLinkCustomRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    /**
     * 프로젝트 링크 파라미터 true 가져오기
     * @param projectId
     * @return FoundationResponse
     */
    public Flux<ProjectLink> findByProjectLinkList(String projectId)
    {
        Query query = new Query();

        query.addCriteria(Criteria.where("project_id").is(projectId)
                .andOperator(Criteria.where("use_yn").is(true))
        );
        return reactiveMongoTemplate.find(query, ProjectLink.class);
    }

    /**
     * 프로젝트 링크 파라미터 true 가져오기
     * @param projectId
     * @param linkProjectId
     * @return FoundationResponse
     */
    public Flux<ProjectLink> findByLinkProject(String projectId, String linkProjectId)
    {
        Query query = new Query();

        query.addCriteria(Criteria.where("project_id").is(projectId))
                .addCriteria(Criteria.where("link_project_id").is(linkProjectId))
                .addCriteria(Criteria.where("use_yn").is(true));
        return reactiveMongoTemplate.find(query, ProjectLink.class);
    }

}
