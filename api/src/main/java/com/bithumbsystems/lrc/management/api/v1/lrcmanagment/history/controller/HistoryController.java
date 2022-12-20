package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.model.request.HistoryRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.model.response.HistoryResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * The type History controller.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/lrcmanagment/project")
@Tag(name = "변경 히스토리", description = "변경 히스토리 API")
public class HistoryController {

  private final HistoryService historyService;

  /**
   * 히스토리 모두 가져오기.
   *
   * @param projectId the project id
   * @param keyword   the keyword
   * @return HistoryResponse history
   */
  @GetMapping("/history")
  @Operation(summary = "거래지원 관리 - 히스토리 모두 가져오기", description = "히스토리 로그 목록 정보를 조회합니다.",
      tags = "사이트 운영 > 거래지원 관리 > 변경 히스토리 > 검색")
  public ResponseEntity<Mono<SingleResponse<List<HistoryResponse>>>> getHistory(
      @Parameter(name = "projectId", description = "프로젝트 코드", in = ParameterIn.QUERY) @RequestParam() String projectId,
      @Parameter(name = "keyword", description = "프로젝트명,심볼 조건 검색", in = ParameterIn.QUERY) @RequestParam() String keyword) {
    return ResponseEntity.ok().body(historyService.getHistory(projectId, keyword).map(SingleResponse::new));
  }


  /**
   * 히스토리 1개 저장하기.
   *
   * @param historyRequest the history request
   * @return HistoryResponse response entity
   */
  @PostMapping("/history")
  @Operation(summary = "거래지원 관리 - 히스토리 1개 저장하기", description = "히스토리 로그 정보 저장합니다.",
      tags = "사이트 운영 > 거래지원 관리 > 변경 히스토리 > 저장")
  public ResponseEntity<Mono<SingleResponse<HistoryResponse>>> createHistory(@RequestBody HistoryRequest historyRequest) {
    return ResponseEntity.ok().body(historyService.create(historyRequest).map(SingleResponse::new));
  }
}
