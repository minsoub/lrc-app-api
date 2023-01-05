package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.model.entity.FoundationInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The interface Foundation info repository.
 */
@Repository
public interface FoundationInfoRepository extends ReactiveMongoRepository<FoundationInfo, String> {

  Mono<FoundationInfo> findById(String id);

  /**
   * Exists by name mono.
   *
   * @param name the name
   * @return the mono
   */
  Mono<Boolean> existsByName(String name);

  /**
   * 생성자 ID로 프로젝트 리스트 조회.
   *
   * @param createAccountId the create account id
   * @return the flux
   */
  Flux<FoundationInfo> findByCreateAccountId(String createAccountId);
}
