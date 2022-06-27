package com.bithumbsystems.persistence.mongodb.lrcmanagment.foundation.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.foundation.model.entity.Foundation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FoundationRepository extends ReactiveMongoRepository<Foundation, String> {

    Mono<Foundation> findByProjectId(String projectId);

    Flux<Foundation> findBySymbolLike(String symbol);

    Mono<Foundation> findByProjectIdAndSymbol(String projectId, String symbol);
}
