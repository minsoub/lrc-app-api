package com.bithumbsystems.lrc.management.api.v1.audit.controller;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.audit.exception.AuditLogException;
import com.bithumbsystems.lrc.management.api.v1.audit.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/service")
@Tag(name = "서비스 로그 관리", description = "서비스 로그 관리 API")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping("/logs")
    @Operation(summary = "서비스 로그 조회" , description = "서비스 로그 관리 목록 정보를 조회합니다.")
    public ResponseEntity<Mono<?>> getAuditLog(@Parameter(name = "fromDate", description = "fromDate 이전 날짜(* 날짜 입력 형식 2022-02-22)", required = true) @RequestParam(required = false) String fromDate,
                                               @Parameter(name = "toDate", description = "toDate 다음 날짜(* 날짜 입력 형식 2022-02-22)", required = true) @RequestParam(required = false) String toDate,
                                               @Parameter(name = "keyword", description = "로그 관련 키워드 조건 검색") @RequestParam(required = false) String keyword,
                                               @Parameter(hidden = true) @CurrentUser Account account) {

        LocalDate nFromDate = LocalDate.parse(fromDate);
        LocalDate nToDate = LocalDate.parse(toDate);

        if(!nToDate.isBefore(LocalDate.parse(fromDate).plusDays(91)))   //최대 3개월
            throw new AuditLogException(ErrorCode.INVALID_DATE_AFTER);


        return ResponseEntity.ok().body(auditLogService.findAuditServiceLog(nFromDate, nToDate, keyword, account.getMySiteId())
                .map(SingleResponse::new));
    }

}
