package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.model.entity.ProjectInfo;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.repository.ProjectInfoCustomRepository;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.repository.ProjectInfoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type Project info domain service.
 */
@Service
@RequiredArgsConstructor
public class ProjectInfoDomainService {

  private final ProjectInfoRepository projectInfoRepository;

  private final ProjectInfoCustomRepository projectInfoCustomRepository;

  /**
   * 프로젝트 정보 1개 id 찾기.
   *
   * @param projectId the project id
   * @return ProjectInfoResponse Object
   */
  public Mono<ProjectInfo> findByProjectId(String projectId) {
    return projectInfoRepository.findByProjectId(projectId);
  }

  /**
   * 프로젝트 정보 1개 저장.
   *
   * @param projectInfo the project info
   * @return ProjectInfoResponse Object
   */
  public Mono<ProjectInfo> save(ProjectInfo projectInfo) {
    return projectInfoRepository.insert(projectInfo);
  }

  /**
   * 프로젝트 정보 업데이트.
   *
   * @param projectInfo the project info
   * @return ProjectInfoResponse Object
   */
  public Mono<ProjectInfo> updateProjectInfo(ProjectInfo projectInfo) {
    return projectInfoRepository.save(projectInfo);
  }

  /**
   * 프로젝트 정보 검색.
   *
   * @param keyword the keyword
   * @return ProjectInfoResponse Object
   */
  public Flux<ProjectInfo> findAllByCustomBusinessNetwork(String keyword) {
    return projectInfoCustomRepository.findAllByCustomBusinessNetwork(keyword);
  }

  /**
   * 프로젝트 정보 검색.
   *
   * @param projectId    the project id
   * @param businessCode the business code
   * @param networkCode  the network code
   * @return ProjectInfoResponse Object
   */
  public Flux<ProjectInfo> findByProjectInfo(String projectId, List<String> businessCode, List<String> networkCode) {
    return projectInfoCustomRepository.findByCustomProjectInfo(projectId, businessCode, networkCode);
  }

  /**
   * Find all flux.
   *
   * @return the flux
   */
  public Flux<ProjectInfo> findAll() {
    return projectInfoRepository.findAll();
  }
}
