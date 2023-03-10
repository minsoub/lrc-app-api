package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.controller;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.core.util.DateUtil;
import com.bithumbsystems.lrc.management.api.v1.audit.exception.AuditLogException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.model.request.FoundationRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.service.FoundationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/lrcmanagment/project")
@Tag(name = "재단", description = "재단 API")
public class FoundationController {

    private final FoundationService foundationService;

    /**
     * 재단 가져오기
     * @return FoundationResponse
     */
    @GetMapping("/foundation")
    @Operation(summary = "거래지원 관리 - 재단 가져오기", description = "재단 목록 정보를 조회합니다.", tags = "사이트 운영  > 거래지원 관리 > 거래지원 검색")
    public ResponseEntity<Mono<?>> getFoundation(@Parameter(name = "keyWord", description = "심볼 검색", in = ParameterIn.QUERY) @RequestParam(required = true) String keyWord) {
        return ResponseEntity.ok().body(foundationService.getFoundationKeyWordSearch(keyWord)
                .map(c -> new MultiResponse(c))
        );
    }

    /**
     * 재단 계약 상태 가져오기
     * @return FoundationResponse
     */
    @GetMapping("/foundation/{contractCode}")
    @Operation(summary = "거래지원 관리 - 재단 계약 상태 가져오기", description = "재단 계약 상태 목록 정보를 조회합니다.", tags = "사이트 운영  > 거래지원 관리 > 재단 계약상태 검색")
    public ResponseEntity<Mono<?>> getFoundationContract(@PathVariable String contractCode) {
        return ResponseEntity.ok().body(foundationService.getFoundation(contractCode)
                .map(c -> new MultiResponse(c))
        );
    }

    /**
     * 재단 1개 저장
     * @param foundationRequest
     * @return FoundationResponse
     */
    @PostMapping("/foundation")
    @Operation(hidden = true, summary = "거래지원 관리 - 재단 1개 저장", description = "재단 정보를 저장합니다.")
    public ResponseEntity<Mono<?>> createFoundation(@Parameter(name = "foundation Object", description = "재단의 Model", in = ParameterIn.QUERY)
                                                        @RequestBody FoundationRequest foundationRequest) {
        return ResponseEntity.ok().body(foundationService.create(foundationRequest)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 재단 1개 저장
     * @param foundationRequest
     * @return FoundationResponse
     */
    @PostMapping("/foundation/{projectId}")
    @Operation(hidden = true, summary = "거래지원 관리 - 재단 1개 저장", description = "재단 정보를 저장합니다.")
    public ResponseEntity<Mono<?>> createFoundation1(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.QUERY)
                                                         @PathVariable("projectId") String projectId,
                                                     @Parameter(name = "foundation Object", description = "재단의 Model", in = ParameterIn.QUERY)
                                                     @RequestBody FoundationRequest foundationRequest) {
        return ResponseEntity.ok().body(foundationService.updateFoundationInfo(projectId, foundationRequest)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 재단 검색 하기
     * @param fromDate 이전
     * @param toDate 다음
     * @param contractCode 계약상태
     * @param progressCode 진행상태
     * @param businessCode 사업계열
     * @param networkCode 네트워크계열
     * @param keyword 프로젝트명,심볼 조건 검색
     * @return Foundation Object
     */
    @GetMapping("foundation/search")
    @Operation(summary = "거래지원 관리 - 재단 검색 하기", description = "재단 정보를 저장합니다.", tags = "사이트 운영 > 거래지원 관리 > 재단 검색")
    public ResponseEntity<Mono<?>> getFoundationSearch(@Parameter(name = "fromDate", description = "fromDate 이전 날짜", required = true) @RequestParam(required = false) String fromDate,
                                              @Parameter(name = "toDate", description = "toDate 다음 날짜", required = true) @RequestParam(required = false) String toDate,
                                              @Parameter(name = "contractCode", description = "계약상태", in = ParameterIn.QUERY) @RequestParam(required = false) String contractCode,
                                              @Parameter(name = "progressCode", description = "진행상태", in = ParameterIn.QUERY) @RequestParam(required = false) String progressCode,
                                              @Parameter(name = "businessCode", description = "사업계열", in = ParameterIn.QUERY) @RequestParam(required = false) String businessCode,
                                              @Parameter(name = "networkCode", description = "네트워크계열", in = ParameterIn.QUERY) @RequestParam(required = false) String networkCode,
                                              @Parameter(name = "keyword", description = "프로젝트명,심볼 조건 검색", in = ParameterIn.QUERY) @RequestParam(required = false) String keyword)
            throws UnsupportedEncodingException {

        LocalDate nFromDate = LocalDate.parse(fromDate);
        LocalDate nToDate = LocalDate.parse(toDate);

        if(DateUtil.isAfter(nFromDate, nToDate))
            throw new AuditLogException(ErrorCode.INVALID_DATE_DAY_PREVIOUS);

//        if(DateUtil.isBetterThenPrevious(nFromDate, nToDate, 3))    //최대 3개월
//            throw new AuditLogException(ErrorCode.INVALID_DATE_MONTH_AFTER);

        nToDate = nToDate.plusDays(1);

        List<String> business = new ArrayList<String>();
        if(StringUtils.isNotEmpty(businessCode)) {
            business = Arrays.asList(URLDecoder.decode(businessCode, "UTF-8").split(";"));
        }

        List<String> network = new ArrayList<String>();
        if(StringUtils.isNotEmpty(networkCode)) {
            network = Arrays.asList(URLDecoder.decode(networkCode, "UTF-8").split(";"));
        }

        return ResponseEntity.ok().body(foundationService.getFoundationSearch(nFromDate, nToDate, contractCode, progressCode, business, network, keyword)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 재단 검색 하기
     * @param fromDate 이전
     * @param toDate 다음
     * @param contractCode 계약상태
     * @param progressCode 진행상태
     * @param businessCode 사업계열
     * @param networkCode 네트워크계열
     * @param keyword 프로젝트명,심볼 조건 검색
     * @return Foundation Object
     */
    @GetMapping("foundation/excel/download")
    @Operation(summary = "거래지원 관리 - 재단 검색 데이터 Excel download", description = "재단 정보 Excel download.", tags = "사이트 운영 > 거래지원 관리 > 재단 검색")
    public Mono<ResponseEntity<?>> downloadExcel(@Parameter(name = "fromDate", description = "fromDate 이전 날짜", required = true) @RequestParam(required = false) String fromDate,
                                                       @Parameter(name = "toDate", description = "toDate 다음 날짜", required = true) @RequestParam(required = false) String toDate,
                                                       @Parameter(name = "contractCode", description = "계약상태", in = ParameterIn.QUERY) @RequestParam(required = false) String contractCode,
                                                       @Parameter(name = "progressCode", description = "진행상태", in = ParameterIn.QUERY) @RequestParam(required = false) String progressCode,
                                                       @Parameter(name = "businessCode", description = "사업계열", in = ParameterIn.QUERY) @RequestParam(required = false) String businessCode,
                                                       @Parameter(name = "networkCode", description = "네트워크계열", in = ParameterIn.QUERY) @RequestParam(required = false) String networkCode,
                                                       @Parameter(name = "keyword", description = "프로젝트명,심볼 조건 검색", in = ParameterIn.QUERY) @RequestParam(required = false) String keyword)
            throws UnsupportedEncodingException {

        LocalDate nFromDate = LocalDate.parse(fromDate);
        LocalDate nToDate = LocalDate.parse(toDate);

        if(DateUtil.isAfter(nFromDate, nToDate))
            throw new AuditLogException(ErrorCode.INVALID_DATE_DAY_PREVIOUS);

//        if(DateUtil.isBetterThenPrevious(nFromDate, nToDate, 3))    //최대 3개월
//            throw new AuditLogException(ErrorCode.INVALID_DATE_MONTH_AFTER);

        nToDate = nToDate.plusDays(1);

        List<String> business = new ArrayList<String>();
        if(StringUtils.isNotEmpty(businessCode)) {
            business = Arrays.asList(URLDecoder.decode(businessCode, "UTF-8").split(";"));
        }

        List<String> network = new ArrayList<String>();
        if(StringUtils.isNotEmpty(networkCode)) {
            network = Arrays.asList(URLDecoder.decode(networkCode, "UTF-8").split(";"));
        }

        return foundationService.downloadExcel(nFromDate, nToDate, contractCode, progressCode, business, network, keyword)
                .flatMap(inputStream -> {
                    HttpHeaders headers = new HttpHeaders();
                    String fileName = "거래지원관리.xlsx";
                    headers.setContentDispositionFormData(fileName, fileName);
                    return Mono.just(ResponseEntity.ok().cacheControl(CacheControl.noCache())
                            .headers(headers)
                            .body(new InputStreamResource(inputStream)));
                });
    }
}
