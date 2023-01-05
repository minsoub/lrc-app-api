package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.user.controller;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.lrc.management.api.core.util.DateUtil;
import com.bithumbsystems.lrc.management.api.v1.dashboard.exception.DashBoardException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.user.model.request.UserRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.user.model.response.UserResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.user.service.UserService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.user.model.enums.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("lrc/lrcmanagment/user")
@RequiredArgsConstructor
@Tag(name = "재단 사용자 관리", description = "재단 사용자 관리 API")
public class UserController {

  private final UserService userService;

  /**
   * 재단 사용자 리스트 검색 조회.
   *
   * @param fromDate   the from date
   * @param toDate     the to date
   * @param userStatus the user status
   * @param keyword    the keyword
   * @return the list
   */
  @GetMapping("")
  @Operation(summary = "재단 사용자 관리 - 다건 조회", description = "검색 조건에 따른 재단 사용자 정보를 조회합니다. (개인정보 마스킹 처리)", tags = "")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "404", description = "NOT FOUND")
  })
  public ResponseEntity<Mono<MultiResponse<UserResponse>>> getList(
      @Parameter(name = "fromDate", description = "검색조건 - 해당일자 이후", example = "2022-01-01") @RequestParam(required = false) String fromDate,
      @Parameter(name = "toDate", description = "검색조건 - 해당일자 까지", example = "2022-01-01") @RequestParam(required = false) String toDate,
      @Parameter(name = "userStatus", description = "검색조건 - 계정상태", example = "NORMAL") @RequestParam(required = false) UserStatus userStatus,
      @Parameter(name = "keyword", description = "검색조건 - 검색어", example = "아무거나") @RequestParam(required = false) String keyword
  ) {
    LocalDate searchFromDate = null;
    LocalDate searchToDate = null;
    if (fromDate != null && toDate != null) {
      searchFromDate = LocalDate.parse(fromDate);
      searchToDate = LocalDate.parse(toDate);
      if (Boolean.TRUE.equals(DateUtil.isAfter(searchFromDate, searchToDate))) {
        throw new DashBoardException(ErrorCode.INVALID_DATE_DAY_PREVIOUS);
      }
      searchToDate = searchToDate.plusDays(1);
    }

    return ResponseEntity.ok().body(userService.getList(searchFromDate, searchToDate, userStatus, keyword)
        .map(MultiResponse::new)
    );
  }

  /**
   * Gets list no masking.
   *
   * @return the list no masking
   */
  @GetMapping("/status-code")
  @Operation(summary = "재단 사용자 관리 - 계정상태 리스트 조회", description = "검색조건 계정상태 리스트", tags = "")
  public ResponseEntity<Mono<MultiResponse<UserRequest>>> getStatusCodeList() {
    return null;
  }

  /**
   * Gets list no masking.
   *
   * @return the list no masking
   */
  @GetMapping("/masking") //TODO 어케하나!
  @Operation(summary = "재단 사용자 관리 - 다건 조회 - 마스킹 해제", description = "검색 조건에 따른 재단 사용자 정보를 조회합니다. (개인정보 마스킹 해제)", tags = "")
  public ResponseEntity<Mono<MultiResponse<UserRequest>>> getListNoMasking() {
    return null;
  }

  /**
   * Update many response entity.
   *
   * @return the response entity
   */
  @PutMapping("")
  @Operation(summary = "재단 사용자 관리 - 로그인 제한/제한 해제", description = "재단 사용자 로그인 차단, 또는 차단 해제", tags = "")
  public ResponseEntity<Mono<MultiResponse<UserRequest>>> updateMany() {
    return null;
  }
}
