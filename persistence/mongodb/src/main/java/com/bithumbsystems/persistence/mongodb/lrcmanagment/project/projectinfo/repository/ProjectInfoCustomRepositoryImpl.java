package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.model.entity.ProjectInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProjectInfoCustomRepositoryImpl implements ProjectInfoCustomRepository{

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    /**
     * 프로젝트 정보 검색
     * @param projectId
     * @param businessCode
     * @param networkCode
     * @return ProjectInfoResponse Object
     */
    public Flux<ProjectInfo> findByProjectInfo(String projectId, List<String> businessCode, List<String> networkCode) {

        Query query = new Query();

        query.addCriteria(Criteria.where("project_id").is(projectId));

        if(businessCode.size() > 0) {
            query.addCriteria(Criteria.where("business_code").in(businessCode));        //사업 계열
        }

        if(networkCode.size() > 0) {
            query.addCriteria(Criteria.where("network_code").in(networkCode));          //네트워크 계열
        }

        return reactiveMongoTemplate.find(query, ProjectInfo.class);
    }
}
