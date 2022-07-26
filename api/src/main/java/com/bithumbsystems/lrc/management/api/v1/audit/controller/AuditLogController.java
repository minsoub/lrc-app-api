package com.bithumbsystems.lrc.management.api.v1.audit.controller;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.core.util.DateUtil;
import com.bithumbsystems.lrc.management.api.v1.audit.exception.AuditLogException;
import com.bithumbsystems.lrc.management.api.v1.audit.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/service")
@Tag(name = "서비스 로그 관리", description = "서비스 로그 관리 API")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping("/logs")
    @Operation(summary = "서비스 로그 관리 - 서비스 로그 조회" , description = "서비스 로그 관리 목록 정보를 조회합니다.", tags = "사이트 관리 > 서비스 로그 관리 > 검색")
    public ResponseEntity<Mono<?>> getAuditLog(@Parameter(name = "fromDate", description = "fromDate 이전 날짜(* 날짜 입력 형식 2022-02-22)", required = true) @RequestParam(required = false) String fromDate,
                                               @Parameter(name = "toDate", description = "toDate 다음 날짜(* 날짜 입력 형식 2022-02-22)", required = true) @RequestParam(required = false) String toDate,
                                               @Parameter(name = "keyword", description = "로그 관련 키워드 조건 검색") @RequestParam(required = false) String keyword,
                                               @Parameter(hidden = true) @CurrentUser Account account) {

        LocalDate nFromDate = LocalDate.parse(fromDate);
        LocalDate nToDate = LocalDate.parse(toDate);

        if(DateUtil.isAfter(nFromDate, nToDate))
            throw new AuditLogException(ErrorCode.INVALID_DATE_DAY_PREVIOUS);

        if(DateUtil.isBetterThenPrevious(nFromDate, nToDate, 3))    //최대 3개월
            throw new AuditLogException(ErrorCode.INVALID_DATE_MONTH_AFTER);

        nToDate = nToDate.plusDays(1);
        return ResponseEntity.ok().body(auditLogService.findAuditServiceLog(nFromDate, nToDate, keyword, account.getMySiteId())
                .map(SingleResponse::new));
    }

    /**
     * 서비스 로그 상세 정보를 조회한다.
     * @param id
     * @param account
     * @return
     */
    @GetMapping("/logs/{id}")
    @Operation(summary = "서비스 로그 관리 - 서비스 상세 로그 조회" , description = "서비스 로그 관리 상세 정보를 조회합니다.", tags = "사이트 관리 > 서비스 로그 관리 > id 검색")
    public ResponseEntity<Mono<?>> getDetailAuditLog(@Parameter(name = "id", description = "id 정보", in = ParameterIn.PATH)
                                                   @PathVariable("id") String id,
                                               @Parameter(hidden = true) @CurrentUser Account account) {

        return ResponseEntity.ok().body(auditLogService.findAuditServiceLogDetail(id)
                .map(SingleResponse::new));
    }

    @GetMapping(value = "/logs/excel/export", produces = APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "서비스 로그 관리 - 엑셀 다운로드", description = "사기 신고 관리: 엑셀 다운로드", tags = "사기 신고 관리")
    public Mono<ResponseEntity<?>> downloadExcel(@Parameter(name = "fromDate", description = "fromDate 이전 날짜(* 날짜 입력 형식 2022-02-22)", required = true) @RequestParam(required = false) String fromDate,
                                                 @Parameter(name = "toDate", description = "toDate 다음 날짜(* 날짜 입력 형식 2022-02-22)", required = true) @RequestParam(required = false) String toDate,
                                                 @Parameter(name = "keyword", description = "로그 관련 키워드 조건 검색") @RequestParam(required = false) String keyword,
                                                 @Parameter(hidden = true) @CurrentUser Account account) {
        LocalDate nFromDate = LocalDate.parse(fromDate);
        LocalDate nToDate = LocalDate.parse(toDate);

        return auditLogService.downloadExcel(nFromDate, nToDate, keyword, account.getMySiteId())
                .flatMap(inputStream -> {
                    HttpHeaders headers = new HttpHeaders();
                    String fileName = "서비스로그.xlsx";
                    headers.setContentDispositionFormData(fileName, fileName);
                    return Mono.just(ResponseEntity.ok().cacheControl(CacheControl.noCache())
                            .headers(headers)
                            .body(new InputStreamResource(inputStream)));
                });
    }

}
