package com.bithumbsystems.persistence.mongodb.lrcmanagment.history.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.model.entity.History;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HistoryDomainService {

    private final HistoryRepository historyRepository;

    /**
     * 히스토리 모두 가져오기
     * @return HistoryResponse
     */
    public Flux<History> findAll() {
        return historyRepository.findAll();
    }

    /**
     * 히스토리 1개 저장하기
     * @return HistoryResponse
     */
    public Mono<History> save(History history) {
        history.setUpdateDate(LocalDateTime.now());
        history.setUpdateAdminAccountId("업데이트 사용자");
        return historyRepository.save(history);
    }
}
