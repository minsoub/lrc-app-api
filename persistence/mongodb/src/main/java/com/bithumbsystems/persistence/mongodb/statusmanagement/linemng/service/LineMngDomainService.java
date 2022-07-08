package com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.service;

import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.model.entity.LineMng;
import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.repository.LineMngRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LineMngDomainService {

    private final LineMngRepository lineMngRepository;

    /**
     * 계열관리 모두 가져오기
     * @return BusinessList
     */
    public Flux<LineMng> findAll() {
        return lineMngRepository.findAll();
    }

    /**
     * 계여롼리 1개 저장
     * @param lineData
     * @return BusinessList
     */
    public Mono<LineMng> save(LineMng lineData) {
        return lineMngRepository.insert(lineData);
    }

    /**
     * 계열관리 업데이트
     * @param lineData
     * @return BusinessList
     */
    public Mono<LineMng> updateLine(LineMng lineData) {
        lineData.setUpdateDate(LocalDateTime.now());
        return lineMngRepository.save(lineData);
    }

    /**
     * 계열관리 id 찾기
     * @param id
     * @return BusinessList
     */
    public Mono<LineMng> findById(String id) {
        return lineMngRepository.findById(id);
    }

    /**
     * 계열관리 1개 삭제
     * @param businessList
     * @return null
     */
    public Mono<Void> deleteBusiness(LineMng businessList) {
        return lineMngRepository.delete(businessList);
    }

}
