package com.bithumbsystems.persistence.mongodb.lrcmanagment.foundation.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.foundation.model.entity.Foundation;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.foundation.repository.FoundationCustomRepository;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.foundation.repository.FoundationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoundationDomainService {

    private final FoundationRepository foundationRepository;

    private final FoundationCustomRepository foundationCustomRepository;

    /**
     * 재단 모두 가져오기
     * @return Foundation Object
     */
    public Flux<Foundation> findAll() {
        return foundationRepository.findAll();
    }

    /**
     * 재단 id로 찾기
     * @param projectId
     * @return Foundation Object
     */
    public Mono<Foundation> findByProjectId(String projectId) {
        return foundationRepository.findByProjectId(projectId);
    }

    /**
     * 재단 symbol로 찾기
     * @param symbol
     * @return Foundation Object
     */
    public Flux<Foundation> findBySymbolLike(String symbol) {
        return foundationRepository.findBySymbolLike(symbol);
    }

    /**
     * 재단 1개 저장
     * @param foundation
     * @return Foundation Object
     */
    public Mono<Foundation> save(Foundation foundation) {
        foundation.setIpoDate(LocalDateTime.now());
        foundation.setCreateDate(LocalDateTime.now());
        return foundationRepository.save(foundation);
    }

    /**
     * 재단 검색 하기개 저장
     * @param fromDate 이전
     * @param toDate 다음
     * @param contractCode 계약상태
     * @param progressCode 진행상태
     * @param business 사업계열
     * @param network 네트워크계열
     * @param keyword 프로젝트명,심볼 조건 검색
     * @return Foundation Object
     */
    public Flux<Foundation> findSearch(LocalDateTime fromDate, LocalDateTime toDate, String contractCode, String progressCode,
                                       List<String> business, List<String> network, String keyword)
    {
        return foundationCustomRepository.findBySearch(fromDate, toDate, contractCode, progressCode, business, network, keyword);
    }
}
