package com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.repository;

import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.model.entity.LineMng;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The interface Line mng repository.
 */
@Repository
public interface LineMngCustomRepository {
  /**
   * Find by id with parent info mono.
   *
   * @param id the id
   * @return the mono
   */
  Mono<LineMng> findByIdWithParentInfo(String id);
}
