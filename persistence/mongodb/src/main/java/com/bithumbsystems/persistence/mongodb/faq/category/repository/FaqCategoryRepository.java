package com.bithumbsystems.persistence.mongodb.faq.category.repository;

import com.bithumbsystems.persistence.mongodb.faq.category.model.entity.FaqCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FaqCategoryRepository extends ReactiveMongoRepository<FaqCategory, String> {

    /**
     * code 찾기
     * @param code
     * @return FaqContentResponse Object
     */
    Mono<FaqCategory> findByCode(String code);

    /**
     * 페이징 데이터 만들기
     * @param pageable
     * @return FaqContentResponse paging
     */
    Flux<FaqCategory> findAllBy(Pageable pageable);
}
