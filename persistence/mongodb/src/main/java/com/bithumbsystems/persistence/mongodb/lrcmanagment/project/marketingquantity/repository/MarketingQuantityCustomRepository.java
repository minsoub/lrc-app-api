package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.marketingquantity.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.marketingquantity.model.entity.MarketingQuantity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MarketingQuantityCustomRepository {

    /**
     * 사용가능한 데이터를 조회한다.
     * @param projectId
     * @return
     */
    public Flux<MarketingQuantity> findByUseData(String projectId);
}
