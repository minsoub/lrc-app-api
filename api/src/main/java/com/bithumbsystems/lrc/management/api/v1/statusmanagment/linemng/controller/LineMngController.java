package com.bithumbsystems.lrc.management.api.v1.statusmanagment.linemng.controller;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.lrc.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.linemng.model.request.LineMngRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.linemng.model.response.LineMngResponse;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.linemng.service.LineMngService;
import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.model.enums.LineType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * The type Line mng controller.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "lrc/statusmanagment", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "계열 관리", description = "계열 관리 API")
public class LineMngController {

  private final LineMngService lineMngService;

  /**
   * 계열관리 모두 가져오기.
   *
   * @param type the type
   * @return BusinessListResponse lines
   */
  @GetMapping("/line-code")
  @Operation(summary = "상태값 관리 - 계열 관리 - 계열 모두 가져오기", description = "계열 목록 정보를 조회합니다.", tags = "사이트 운영 > 상태값 관리 > 계열 관리 > 검색")
  public ResponseEntity<Mono<MultiResponse<LineMngResponse>>> getLines(LineType type) {
    return ResponseEntity.ok().body(lineMngService.findAll(type)
        .collectSortedList(Comparator.comparing(LineMngResponse::getCreateDate).reversed())
        .map(MultiResponse::new)
    );
  }

  /**
   * 계열관리 트리 구조 만들기.
   *
   * @param type  the type
   * @param isUse the is use
   * @return BusinessListResponse lines tree
   */
  @GetMapping("/line-code/tree")
  @Operation(summary = "상태값 관리 - 계열 관리 - 계열 트리 구조 만들기", description = "계열 목록 트리 정보를 조회합니다.", tags = "사이트 운영 > 상태값 관리 > 계열 관리 > 트리구조 만들기")
  public ResponseEntity<Mono<MultiResponse<LineMngResponse>>> getLinesTree(
      @Parameter(description = "계열 구분", example = "BUSINESS") @RequestParam(value = "type", required = false) LineType type,
      @Parameter(description = "사용안함 포함", example = "true", required = true) @RequestParam(defaultValue = "true") Boolean isUse) {
    return ResponseEntity.ok().body(lineMngService.findAllTree(type, isUse)
        .map(MultiResponse::new)
    );
  }

  /**
   * 계열관리 생성.
   *
   * @param lineMngRequest the line mng request
   * @param account        the account
   * @return BusinessListResponse response entity
   */
  @PostMapping("/line-code")
  @Operation(summary = "상태값 관리 - 계열 관리 - 계열관리 1개 저장", description = "계열관리 정보를 저장합니다.", tags = "사이트 운영 > 상태값 관리 > 계열 관리 > 1개 저장")
  public ResponseEntity<Mono<SingleResponse<LineMngResponse>>> createLine(
      @Parameter(name = "business Object", description = "계열관리 Model") @RequestBody LineMngRequest lineMngRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(lineMngService.create(lineMngRequest, account)
        .map(SingleResponse::new)
    );
  }

  /**
   * 계열관리 수정.
   *
   * @param id             the id
   * @param lineMngRequest the line mng request
   * @param account        the account
   * @return BusinessListResponse response entity
   */
  @PutMapping("/line-code/{id}")
  @Operation(summary = "상태값 관리 - 계열 관리 - 계열관리 업데이트", description = "계열관리 정보를 수정합니다.", tags = "사이트 운영 > 상태값 관리 > 계열 관리 > 수정")
  public ResponseEntity<Mono<SingleResponse<LineMngResponse>>> updateLines(
      @Parameter(name = "id", description = "계열관리 id") @PathVariable("id") String id,
      @Parameter(name = "business Object", description = "계열관리 Model") @RequestBody LineMngRequest lineMngRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(lineMngService.updateLine(id, lineMngRequest, account)
        .map(SingleResponse::new)
    );
  }

  /**
   * 계열관리 삭제.
   *
   * @param id      the id
   * @param account the account
   * @return null response entity
   */
  @DeleteMapping("/line-code/{id}")
  @Operation(summary = "상태값 관리 - 계열 관리 - 계열관리 삭제", description = "계열관리 정보를 삭제합니다.", tags = "사이트 운영 > 상태값 관리 > 계열 관리 > 삭제")
  public ResponseEntity<Mono<SingleResponse>> deleteBusiness(@Parameter(name = "id", description = "계열관리 id") @PathVariable("id") String id,
                                                             @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(lineMngService.deleteLine(id, account)
        .then(Mono.just(new SingleResponse()))
    );
  }
}
