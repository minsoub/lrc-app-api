package com.bithumbsystems.persistence.mongodb.faq.category.repository;

import com.bithumbsystems.persistence.mongodb.faq.category.model.entity.FaqCategory;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface FaqCategoryRepository extends ReactiveMongoRepository<FaqCategory, UUID> {

    Mono<FaqCategory> findByCode(String code);

}
