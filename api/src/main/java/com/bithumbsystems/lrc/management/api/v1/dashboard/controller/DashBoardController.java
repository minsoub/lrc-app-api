package com.bithumbsystems.lrc.management.api.v1.dashboard.controller;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.core.util.DateUtil;
import com.bithumbsystems.lrc.management.api.v1.dashboard.exception.DashBoardException;
import com.bithumbsystems.lrc.management.api.v1.dashboard.model.response.DashBoardLineMngResponse;
import com.bithumbsystems.lrc.management.api.v1.dashboard.model.response.DashBoardOverallResponse;
import com.bithumbsystems.lrc.management.api.v1.dashboard.model.response.DashBoardStatusCodeResponse;
import com.bithumbsystems.lrc.management.api.v1.dashboard.service.DashBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * The type Dash board controller.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/dashboard")
@Tag(name = "거래지원 현황 대시보드")
public class DashBoardController {

  private final DashBoardService dashBoardService;


  /**
   * 상태값 모두 가져오기.
   *
   * @param fromDate the from date
   * @param toDate   the to date
   * @return DashBoardStatusCodeResponse status value
   */
  @GetMapping("/status-code")
  @Operation(summary = "거래지원 현황 - 상태값 통계 모두 가져오기", description = "상태값 통계 목록 정보를 조회합니다.", tags = "사이트 운영 > 거래지원 현황 > 상태값 조회")
  public ResponseEntity<Mono<SingleResponse<List<DashBoardStatusCodeResponse>>>> getStatusValue(
      @Parameter(name = "fromDate", description = "fromDate 이전 날짜", required = true) @RequestParam(required = false) String fromDate,
      @Parameter(name = "toDate", description = "toDate 다음 날짜", required = true) @RequestParam(required = false) String toDate
  ) {
    LocalDate searchFromDate = null;
    LocalDate searchToDate = null;
    if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
      searchFromDate = LocalDate.parse(fromDate);
      searchToDate = LocalDate.parse(toDate);
      if (Boolean.TRUE.equals(DateUtil.isAfter(searchFromDate, searchToDate))) {
        throw new DashBoardException(ErrorCode.INVALID_DATE_DAY_PREVIOUS);
      }
      searchToDate = searchToDate.plusDays(1);
    }

    return ResponseEntity.ok().body(dashBoardService.getStatusCode(searchFromDate, searchToDate)
        .map(SingleResponse::new)
    );
  }

  /**
   * 계열관리 통계 모두 가져오기 - 최초 오픈 용 차트 기반 데이터.
   *
   * @return DashBoardLineMngResponse line mng
   */
  @GetMapping("/line-code")
  @Operation(summary = "거래지원 현황 - 계열관리 통계 모두 가져오기", description = "계열관리 통계 목록 정보를 조회합니다.", tags = "사이트 운영 > 거래지원 현황 > 계열관리 조회")
  public ResponseEntity<Mono<SingleResponse<List<DashBoardLineMngResponse>>>> getLineMng() {
    return ResponseEntity.ok().body(dashBoardService.getLineMng()
        .map(SingleResponse::new)
    );
  }

  /**
   * 재단 현황.
   *
   * @return DashBoardLineMngResponse overall status
   */
  @GetMapping("/overall-status")
  @Operation(summary = "재단 현황", description = "재단 현황 정보를 조회합니다.", tags = "사이트 운영 > 거래지원 현황 > 재단현황 조회")
  public ResponseEntity<Mono<SingleResponse<DashBoardOverallResponse>>> getOverallStatus() {
    return ResponseEntity.ok().body(dashBoardService.getOverallStatus()
        .map(SingleResponse::new)
    );
  }
}
