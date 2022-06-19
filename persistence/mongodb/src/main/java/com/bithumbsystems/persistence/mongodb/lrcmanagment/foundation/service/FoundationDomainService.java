package com.bithumbsystems.persistence.mongodb.lrcmanagment.foundation.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.foundation.model.entity.Foundation;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.foundation.repository.FoundationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FoundationDomainService {

    private final FoundationRepository foundationRepository;

    /**
     * 재단정보 모두 가져오기
     * @return Foundation Object
     */
    public Flux<Foundation> findAll() {
        return foundationRepository.findAll();
    }

    /**
     * 재단정보 id로 찾기
     * @param projectId
     * @return Foundation Object
     */
    public Mono<Foundation> findByProjectId(String projectId) {
        return foundationRepository.findByProjectId(projectId);
    }

    /**
     * 재단정보 1개 저장
     * @param foundation
     * @return Foundation Object
     */
    public Mono<Foundation> save(Foundation foundation) {
        foundation.setIpoDate(LocalDateTime.now());
        foundation.setCreateDate(LocalDateTime.now());
        return foundationRepository.save(foundation);
    }
}
