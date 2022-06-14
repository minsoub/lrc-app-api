package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.model.ProjectInfo;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.repository.ProjectInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProjectInfoDomainService {

    private final ProjectInfoRepository projectInfoRepository;

    /**
     * 프로젝트 정보 1개 id 찾기
     * @param projectId
     * @return ProjectInfoResponse Object
     */
    public Mono<ProjectInfo> findByProjectId(String projectId) {
        return projectInfoRepository.findByProjectId(projectId);
    }

    /**
     * 프로젝트 정보 1개 저장
     * @param projectInfo
     * @return ProjectInfoResponse Object
     */
    public Mono<ProjectInfo> save(ProjectInfo projectInfo) {
        return projectInfoRepository.insert(projectInfo);
    }

    /**
     * 프로젝트 정보 업데이트
     * @param projectInfo
     * @return ProjectInfoResponse Object
     */
    public Mono<ProjectInfo> updateProjectInfo(ProjectInfo projectInfo) {
        return projectInfoRepository.save(projectInfo);
    }
}
