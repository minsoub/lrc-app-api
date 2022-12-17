package com.bithumbsystems.persistence.mongodb.lrcmanagment.history.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.model.entity.History;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.repository.HistoryCustomRepository;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.repository.HistoryRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type History domain service.
 */
@Service
@RequiredArgsConstructor
public class HistoryDomainService {
  private final HistoryRepository historyRepository;
  private final HistoryCustomRepository historyCustomRepository;

  /**
   * 히스토리 모두 가져오기.
   *
   * @return HistoryResponse flux
   */
  public Flux<History> findAll() {
    return historyRepository.findAll();
  }

  /**
   * 검색 조건으로 변경 히스토리를 찾는다.
   *
   * @param projectId the project id
   * @param keyword   the keyword
   * @return flux
   */
  public Flux<History> findBySearch(String projectId, String keyword) {
    return historyCustomRepository.findBySearch(projectId, keyword);
  }

  /**
   * 히스토리 1개 저장하기.
   *
   * @param history the history
   * @return HistoryResponse mono
   */
  public Mono<History> save(History history) {
    history.setUpdateDate(LocalDateTime.now());
    return historyRepository.save(history);
  }
}
