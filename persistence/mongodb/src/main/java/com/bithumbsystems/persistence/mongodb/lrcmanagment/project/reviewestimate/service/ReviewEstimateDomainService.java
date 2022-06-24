package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.reviewestimate.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.reviewestimate.model.entity.ReviewEstimate;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.reviewestimate.repository.ReviewEstimateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReviewEstimateDomainService {

    private final ReviewEstimateRepository reviewEstimateRepository;

    /**
     * 검토평가 id로 찾기
     * @param projectId
     * @return MarketingQuantityResponse Object
     */
    public Flux<ReviewEstimate> findByProjectId(String projectId) {
        return reviewEstimateRepository.findByProjectId(projectId);
    }

    /**
     * 검토평가 여러개 저장 및 업데이트
     * @param reviewEstimate
     * @return MarketingQuantityResponse Object
     */
    public Mono<ReviewEstimate> save(ReviewEstimate reviewEstimate) {
        return reviewEstimateRepository.save(reviewEstimate);
    }
}
