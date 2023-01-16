package com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.service;

import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.model.entity.LineMng;
import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.repository.LineMngCustomRepository;
import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.repository.LineMngRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type Line mng domain service.
 */
@Service
@RequiredArgsConstructor
public class LineMngDomainService {

  private final LineMngRepository lineMngRepository;
  private final LineMngCustomRepository lineMngCustomRepository;

  /**
   * 계열관리 모두 가져오기.
   *
   * @return BusinessList flux
   */
  public Flux<LineMng> findAll() {
    return lineMngRepository.findAll();
  }

  /**
   * 계열관리 1개 저장.
   *
   * @param lineData the line data
   * @return BusinessList mono
   */
  public Mono<LineMng> save(LineMng lineData) {
    return lineMngRepository.insert(lineData);
  }

  /**
   * 계열관리 업데이트.
   *
   * @param lineData the line data
   * @return BusinessList mono
   */
  public Mono<LineMng> updateLine(LineMng lineData) {
    lineData.setUpdateDate(LocalDateTime.now());
    return lineMngRepository.save(lineData);
  }

  /**
   * 계열관리 id 찾기.
   *
   * @param id the id
   * @return BusinessList mono
   */
  public Mono<LineMng> findById(String id) {
    return lineMngRepository.findById(id);
  }

  /**
   * 계열관리 1개 삭제.
   *
   * @param businessList the business list
   * @return null mono
   */
  public Mono<Void> deleteBusiness(LineMng businessList) {
    return lineMngRepository.delete(businessList);
  }

  /**
   * 계열관리 트리 구조 만들기.
   *
   * @param parentId the parent id
   * @return the flux
   */
  public Flux<LineMng> findByParentId(String parentId) {
    return lineMngRepository.findByParentId(parentId);
  }

  /**
   * ID로 상위 계열 정보 포함한 계열 정보 조회.
   *
   * @param id the id
   * @return the mono
   */
  public Mono<LineMng> findByIdWithParentInfo(String id) {
    return lineMngCustomRepository.findByIdWithParentInfo(id);
  }

  /**
   * Find all by use yn is true flux.
   *
   * @return the flux
   */
  public Flux<LineMng> findAllByUseYnIsTrue() {
    return lineMngRepository.findAllByUseYnIsTrue();
  }
}
