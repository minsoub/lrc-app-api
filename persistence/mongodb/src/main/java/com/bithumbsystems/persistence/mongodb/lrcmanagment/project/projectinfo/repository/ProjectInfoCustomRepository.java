package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.model.entity.ProjectInfo;
import java.util.List;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * The interface Project info custom repository.
 */
@Repository
public interface ProjectInfoCustomRepository {

  /**
   * 프로젝트 사업계열, 네트워크계열 검색.
   *
   * @param keyword the keyword
   * @return ProjectInfoResponse Object
   */
  Flux<ProjectInfo> findAllByCustomBusinessNetwork(String keyword);

  /**
   * 프로젝트 정보 검색.
   *
   * @param projectId    the project id
   * @param businessCode the business code
   * @param networkCode  the network code
   * @return ProjectInfoResponse Object
   */
  Flux<ProjectInfo> findByCustomProjectInfo(String projectId, List<String> businessCode, List<String> networkCode);
}
