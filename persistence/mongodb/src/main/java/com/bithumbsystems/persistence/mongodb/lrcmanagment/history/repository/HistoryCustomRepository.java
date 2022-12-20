package com.bithumbsystems.persistence.mongodb.lrcmanagment.history.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.model.entity.History;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * The interface History custom repository.
 */
@Repository
public interface HistoryCustomRepository {
  /**
   * Find by search flux.
   *
   * @param projectId the project id
   * @param keyword   the keyword
   * @return the flux
   */
  Flux<History> findBySearch(String projectId, String keyword);
}
