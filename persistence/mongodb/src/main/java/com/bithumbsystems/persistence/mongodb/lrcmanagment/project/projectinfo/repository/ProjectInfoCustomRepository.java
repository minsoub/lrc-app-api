package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.model.entity.ProjectInfo;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface ProjectInfoCustomRepository {

    /**
     * 프로젝트 정보 검색
     * @param projectId
     * @param businessCode
     * @param networkCode
     * @return ProjectInfoResponse Object
     */
    public Flux<ProjectInfo> findByProjectInfo(String projectId, List<String> businessCode, List<String> networkCode);
}
