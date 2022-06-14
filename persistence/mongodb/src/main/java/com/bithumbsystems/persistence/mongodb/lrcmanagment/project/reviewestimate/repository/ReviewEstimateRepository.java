package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.reviewestimate.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.reviewestimate.model.ReviewEstimate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ReviewEstimateRepository extends ReactiveMongoRepository<ReviewEstimate, String> {

    Flux<ReviewEstimate> findByProjectId(String projectId);
}
