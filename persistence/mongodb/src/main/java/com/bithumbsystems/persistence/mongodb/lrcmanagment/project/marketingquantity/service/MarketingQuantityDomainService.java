package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.marketingquantity.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.marketingquantity.model.entity.MarketingQuantity;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.marketingquantity.repository.MarketingQuantityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MarketingQuantityDomainService {

    private final MarketingQuantityRepository marketingQuantityRepository;

    /**
     * 마케팅 수량 id로 찾기
     * @param projectId
     * @return MarketingQuantityResponse Object
     */
    public Flux<MarketingQuantity> findByProjectId(String projectId) {
        return marketingQuantityRepository.findByProjectId(projectId);
    }

    /**
     * 마케팅 수량 여러개 저장 및 업데이트
     * @param marketingQuantity
     * @return MarketingQuantityResponse Object
     */
    public Mono<MarketingQuantity> save(MarketingQuantity marketingQuantity) {
        return marketingQuantityRepository.save(marketingQuantity);
    }
}
