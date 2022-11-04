package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.model.entity.FoundationInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FoundationInfoRepository extends ReactiveMongoRepository<FoundationInfo, String> {

    Mono<FoundationInfo> findById(String id);

    Mono<Boolean> existsByName(String name);
}
