package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.marketingquantity.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.marketingquantity.model.entity.MarketingQuantity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MarketingQuantityRepository extends ReactiveMongoRepository<MarketingQuantity, String> {

    Flux<MarketingQuantity> findByProjectId(String projectId);
}
