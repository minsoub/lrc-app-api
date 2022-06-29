package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.model.request.FoundationRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.service.FoundationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
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
    @Operation(summary = "재단 가져오기", description = "재단 목록 정보를 조회합니다.")
    public ResponseEntity<Mono<?>> getFoundation() {
        return ResponseEntity.ok().body(foundationService.getFoundation1()
                .map(c -> new MultiResponse(c))
        );
    }

    /**
     * 재단 1개 저장
     * @param foundationRequest
     * @return FoundationResponse
     */
    @PostMapping("/foundation")
    @Operation(summary = "재단 1개 저장", description = "재단 정보를 저장합니다.")
    public ResponseEntity<Mono<?>> createFoundation(@Parameter(name = "foundation Object", description = "재단의 Model", in = ParameterIn.PATH)
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
    @Operation(summary = "재단 1개 저장", description = "재단 정보를 저장합니다.")
    public ResponseEntity<Mono<?>> createFoundation1(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH)
                                                         @PathVariable("projectId") String projectId,
                                                     @Parameter(name = "foundation Object", description = "재단의 Model", in = ParameterIn.PATH)
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
     * @param businessList 사업계열
     * @param networkList 네트워크계열
     * @param keyword 프로젝트명,심볼 조건 검색
     * @return Foundation Object
     */
    @GetMapping("foundation/search")
    @Operation(summary = "재단 검색 하기", description = "재단 정보를 저장합니다.")
    public ResponseEntity<Mono<?>> findSearch(@Parameter(name = "fromDate", description = "fromDate 이전 날짜", in = ParameterIn.PATH) @RequestParam(required = false) String fromDate,
                                              @Parameter(name = "toDate", description = "toDate 다음 날짜", in = ParameterIn.PATH) @RequestParam(required = false) String toDate,
                                              @Parameter(name = "contractCode", description = "계약상태", in = ParameterIn.PATH) @RequestParam(required = false) String contractCode,
                                              @Parameter(name = "progressCode", description = "진행상태", in = ParameterIn.PATH) @RequestParam(required = false) String progressCode,
                                              @Parameter(name = "businessList", description = "사업계열", in = ParameterIn.PATH) @RequestParam(required = false) String businessList,
                                              @Parameter(name = "networkList", description = "네트워크계열", in = ParameterIn.PATH) @RequestParam(required = false) String networkList,
                                              @Parameter(name = "keyword", description = "프로젝트명,심볼 조건 검색", in = ParameterIn.PATH) @RequestParam(required = false) String keyword)
            throws UnsupportedEncodingException {

        LocalDateTime nFromDate = LocalDateTime.parse(fromDate);
        LocalDateTime nToDate = LocalDateTime.parse(toDate);

        List<String> business = new ArrayList<String>();
        if(StringUtils.isNotEmpty(businessList)) {
            business = Arrays.asList(URLDecoder.decode(businessList, "UTF-8").split(";"));
        }

        List<String> network = new ArrayList<String>();
        if(StringUtils.isNotEmpty(networkList)) {
            network = Arrays.asList(URLDecoder.decode(networkList, "UTF-8").split(";"));
        }


        return ResponseEntity.ok().body(foundationService.findSearch(nFromDate, nToDate, contractCode, progressCode, business, network, keyword)
                .map(c -> new SingleResponse(c))
        );
    }
}
