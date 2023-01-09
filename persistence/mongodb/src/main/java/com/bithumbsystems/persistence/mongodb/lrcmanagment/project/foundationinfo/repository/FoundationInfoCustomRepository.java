package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.model.entity.FoundationInfo;
import java.time.LocalDate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * The interface Foundation info custom repository.
 */
@Repository
public interface FoundationInfoCustomRepository {

  /**
   * 재단정보 계약 상태 검색.
   *
   * @param contractCode the contract code
   * @return FoundationInfoResponse flux
   */
  Flux<FoundationInfo> findByCustomContract(String contractCode);

  /**
   * 재단정보 계약 상태, 진행 상태 검색.
   *
   * @param keyword        the keyword
   * @param searchFromDate the search from date
   * @param searchToDate   the search to date
   * @return FoundationInfoResponse flux
   */
  Flux<FoundationInfo> findByCustomContractProcess(String keyword, LocalDate searchFromDate, LocalDate searchToDate);

  /**
   * 재단정보 symbol like 검색.
   *
   * @param symbol the symbol
   * @return flux
   */
  Flux<FoundationInfo> findBySymbolSearch(String symbol);

  /**
   * 재단정보 검색 하기.
   *
   * @param fromDate     이전
   * @param toDate       다음
   * @param contractCode 계약상태
   * @param progressCode 진행상태
   * @param keyword      프로젝트명,심볼 조건 검색
   * @return FoundationInfoResponse Object
   */
  Flux<FoundationInfo> findByCustomSearch(LocalDate fromDate, LocalDate toDate, String contractCode, String progressCode, String keyword);

}
