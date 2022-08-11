package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.reviewestimate.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.reviewestimate.model.entity.ReviewEstimate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ReviewEstimateCustomRepository {
    public Flux<ReviewEstimate> findByUseData(String projectId);
}
