package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.model.entity.FoundationInfo;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.repository.FoundationInfoCustomRepository;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.repository.FoundationInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FoundationInfoDomainService {

    private final FoundationInfoRepository foundationInfoRepository;
    private final FoundationInfoCustomRepository foundationInfoCustomRepository;


    /**
     * 재단정보 및 계약 상태 검색
     * @param contractCode
     * @return FoundationInfoResponse
     */
    public Flux<FoundationInfo> findByCustomSearchAll(String contractCode) {
        return foundationInfoCustomRepository.findByCustomSearchAll(contractCode);
    }

    /**
     * 재단정보를 symbol로 like 검색한다.
     *
     * @param symbol
     * @return
     */
    public Flux<FoundationInfo> findBySymbolSearch(String symbol) {
        return foundationInfoCustomRepository.findBySymbolSearch(symbol);
    }

    public Flux<FoundationInfo> findByFoundationInfo() {
        return foundationInfoRepository.findAll();
    }

    /**
     * 재단정보 id로 찾기
     * @param id
     * @return SubmittedDocumentResponse Object
     */
    public Mono<FoundationInfo> findById(String id) {
        return foundationInfoRepository.findById(id);
    }

    /**
     * 재단정보 여러개 저장 및 업데이트
     * @param foundationInfo
     * @return FoundationInfoResponse Object
     */
    public Mono<FoundationInfo> updateFoundationInfo(FoundationInfo foundationInfo) {
        return foundationInfoRepository.save(foundationInfo);
    }

    /**
     * 재단정보 검색 하기
     * @param fromDate 이전
     * @param toDate 다음
     * @param contractCode 계약상태
     * @param progressCode 진행상태
     * @param keyword 프로젝트명,심볼 조건 검색
     * @return FoundationInfoResponse Object
     */
    public Flux<FoundationInfo> findByCustomSearch(LocalDate fromDate, LocalDate toDate, String contractCode, String progressCode, String keyword) {
        return foundationInfoCustomRepository.findByCustomSearch(fromDate, toDate, contractCode, progressCode, keyword);
    }
}
