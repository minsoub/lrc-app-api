package com.bithumbsystems.persistence.mongodb.faq.content.repository;

import com.bithumbsystems.persistence.mongodb.faq.content.model.entity.FaqContent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FaqContentRepository extends ReactiveMongoRepository<FaqContent, String> {

    /**
     * 콘텐츠DB userId 찾기
     * @param userId
     * @return FaqContentResponse
     */
    Mono<FaqContent> findByUserId(String userId);

    /**
     * 콘텐츠DB userId 삭제
     * @param userId
     * @return FaqContentResponse
     */
    Mono<Void> deleteByUserId(String userId);

    /**
     * 페이징 데이터 만들기
     * @param pageable
     * @return FaqContentResponse paging
     */
    Flux<FaqContent> findAllBy(Pageable pageable);


    /**
     * 검색
     * @param keyword
     * @return FaqContent
     */
    Flux<FaqContent> findByTitleLike(String keyword);
}
