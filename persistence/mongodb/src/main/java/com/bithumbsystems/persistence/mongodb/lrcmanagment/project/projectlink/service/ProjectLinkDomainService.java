package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectlink.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectlink.model.entity.ProjectLink;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectlink.repository.ProjectLinkCustomRepository;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectlink.repository.ProjectLinkRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type Project link domain service.
 */
@Service
@RequiredArgsConstructor
public class ProjectLinkDomainService {

  private final ProjectLinkRepository projectLinkRepository;

  private final ProjectLinkCustomRepository projectLinkCustomRepository;

  /**
   * link key를 통해서 데이터를 찾는다.
   *
   * @param id the id
   * @return mono
   */
  public Mono<ProjectLink> findById(String id) {
    return projectLinkRepository.findById(id);
  }

  /**
   * 프로젝트 링크 true 인것 가져오기.
   *
   * @param projectId the project id
   * @return FoundationResponse flux
   */
  public Flux<ProjectLink> findByProjectLinkList(String projectId) {
    return projectLinkCustomRepository.findByProjectLinkList(projectId);
  }

  /**
   * 프로젝트 링크 파라미터 true 가져오기.
   *
   * @param projectId     the project id
   * @param linkProjectId the link project id
   * @return FoundationResponse mono
   */
  public Mono<ProjectLink> findByLinkProject(String projectId, String linkProjectId) {
    return projectLinkCustomRepository.findByLinkProject(projectId, linkProjectId);
  }

  /**
   * 프로젝트 링크 생성.
   *
   * @param projectLink the project link
   * @return FoundationResponse mono
   */
  public Mono<ProjectLink> save(ProjectLink projectLink) {
    projectLink.setId(UUID.randomUUID().toString());
    projectLink.setUseYn(true); //true 인것만
    return projectLinkRepository.insert(projectLink);
  }

  /**
   * 프로젝트 링크 false 저장(링크해제).
   *
   * @param projectLink the project link
   * @return FoundationResponse mono
   */
  public Mono<ProjectLink> deleteLinkProject(ProjectLink projectLink) {
    projectLink.setUseYn(false); //false
    return projectLinkRepository.save(projectLink);
  }
}
