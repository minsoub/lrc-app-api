package com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.repository;

import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.model.entity.LineMng;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * The interface Line mng repository.
 */
@Repository
public interface LineMngRepository extends ReactiveMongoRepository<LineMng, String> {
  /**
   * 계열관리 트리 구조 만들기.
   *
   * @param parentId the parent id
   * @return the flux
   */
  Flux<LineMng> findByParentId(String parentId);

  /**
   * Find all by use yn is true flux.
   *
   * @return the flux
   */
  Flux<LineMng> findAllByUseYnIsTrue();
}
