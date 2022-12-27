package com.bithumbsystems.persistence.mongodb.statusmanagement.statuscode.service;

import com.bithumbsystems.persistence.mongodb.statusmanagement.statuscode.model.entity.StatusCode;
import com.bithumbsystems.persistence.mongodb.statusmanagement.statuscode.repository.StatusCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type Status code domain service.
 */
@Service
@RequiredArgsConstructor
public class StatusCodeDomainService {

  private final StatusCodeRepository statusValueRepository;

  /**
   * 상태값 관리 모두 가져오기.
   *
   * @return StatusValueList flux
   */
  public Flux<StatusCode> findAllStatus() {
    return statusValueRepository.findAll();
  }

  /**
   * 상태값 관리 모두 가져오기.
   *
   * @return StatusValueList flux
   */
  public Flux<StatusCode> findAllTree() {
    return statusValueRepository.findAll();
  }

  /**
   * 상태값 관리 모두 가져오기.
   *
   * @param parentCode the parent code
   * @return StatusValueList flux
   */
  public Flux<StatusCode> findParentCode(String parentCode) {
    return statusValueRepository.findByParentCode(parentCode);
  }

  /**
   * 상태값 관리 1개 찾기.
   *
   * @param id the id
   * @return mono
   */
  public Mono<StatusCode> findStatusValueById(String id) {
    return statusValueRepository.findById(id);
  }

  /**
   * 상태값 관리 1개 저장.
   *
   * @param lrcStatusCode the lrc status code
   * @return StatusValueList mono
   */
  public Mono<StatusCode> save(StatusCode lrcStatusCode) {
    return statusValueRepository.insert(lrcStatusCode);
  }

  /**
   * 상태값 관리 수정.
   *
   * @param lrcStatusCode the lrc status code
   * @return mono
   */
  public Mono<StatusCode> update(StatusCode lrcStatusCode) {
    return statusValueRepository.save(lrcStatusCode);
  }

  /**
   * 상태값 관리 1개 삭제.
   *
   * @param lrcStatusCode the lrc status code
   * @return N /A
   */
  public Mono<Void> deleteStatusValue(StatusCode lrcStatusCode) {
    return statusValueRepository.delete(lrcStatusCode);
  }
}
