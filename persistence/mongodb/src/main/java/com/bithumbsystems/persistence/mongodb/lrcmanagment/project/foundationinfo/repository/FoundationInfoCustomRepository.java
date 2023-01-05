package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.model.entity.FoundationInfo;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Repository
public interface FoundationInfoCustomRepository {

    /**
     * 재단정보 계약 상태 검색
     * @param contractCode
     * @return FoundationInfoResponse
     */
    public Flux<FoundationInfo> findByCustomContract(String contractCode);

    /**
     * 재단정보 계약 상태, 진행 상태 검색
     * @param keyword
     * @return FoundationInfoResponse
     */
    public Flux<FoundationInfo> findByCustomContractProcess(String keyword, LocalDate searchFromDate, LocalDate searchToDate);

    /**
     * 재단정보를 symbol로 like 검색한다.
     *
     * @param symbol
     * @return
     */
    public Flux<FoundationInfo> findBySymbolSearch(String symbol);

    /**
     * 재단정보 검색 하기
     * @param fromDate 이전
     * @param toDate 다음
     * @param contractCode 계약상태
     * @param progressCode 진행상태
     * @param keyword 프로젝트명,심볼 조건 검색
     * @return FoundationInfoResponse Object
     */
    public Flux<FoundationInfo> findByCustomSearch(LocalDate fromDate, LocalDate toDate, String contractCode, String progressCode, String keyword);

}
