package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.marketingquantity.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.marketingquantity.model.entity.MarketingQuantity;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.marketingquantity.repository.MarketingQuantityCustomRepository;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.marketingquantity.repository.MarketingQuantityRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type Marketing quantity domain service.
 */
@Service
@RequiredArgsConstructor
public class MarketingQuantityDomainService {

  private final MarketingQuantityRepository marketingQuantityRepository;
  private final MarketingQuantityCustomRepository marketingQuantityCustomRepository;

  /**
   * 마케팅 수량 id로 찾기.
   *
   * @param projectId the project id
   * @return MarketingQuantityResponse Object
   */
  public Flux<MarketingQuantity> findByProjectId(String projectId) {
    return marketingQuantityRepository.findByProjectId(projectId);
  }

  /**
   * 사용가능한 마케팅 수량을 조회한다.
   *
   * @param projectId the project id
   * @return flux
   */
  public Flux<MarketingQuantity> findByUseData(String projectId) {
    return marketingQuantityCustomRepository.findByUseData(projectId);
  }

  /**
   * 마케팅 수량 리턴.
   *
   * @param id the id
   * @return mono
   */
  public Mono<MarketingQuantity> findById(String id) {
    return marketingQuantityRepository.findById((id));
  }

  /**
   * 마케팅 수량 여러개 저장 및 업데이트.
   *
   * @param marketingQuantity the marketing quantity
   * @return MarketingQuantityResponse Object
   */
  public Mono<MarketingQuantity> save(MarketingQuantity marketingQuantity) {
    return marketingQuantityRepository.save(marketingQuantity);
  }

  /**
   * 마케팅 수량 신규 등록.
   *
   * @param marketingQuantity the marketing quantity
   * @return mono
   */
  public Mono<MarketingQuantity> insert(MarketingQuantity marketingQuantity) {
    marketingQuantity.setId(UUID.randomUUID().toString());
    return marketingQuantityRepository.save(marketingQuantity);
  }
}
