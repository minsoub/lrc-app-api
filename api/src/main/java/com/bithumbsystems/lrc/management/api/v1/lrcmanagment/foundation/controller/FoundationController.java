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
        return ResponseEntity.ok().body(foundationService.getFoundation()
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

    @GetMapping("foundation/search")
    public ResponseEntity<Mono<?>> findSearch(@RequestParam(required = false) String fromDate,
                                              @RequestParam(required = false) String toDate,
                                              @RequestParam(required = false) String contrectCode,
                                              @RequestParam(required = false) String progressCode,
                                              @RequestParam(required = false) String businessList,
                                              @RequestParam(required = false) String networkList,
                                              @RequestParam(required = false) String keyword) throws UnsupportedEncodingException {

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


        return ResponseEntity.ok().body(foundationService.findSearch(nFromDate, nToDate, contrectCode, progressCode, business, network, keyword)
                .map(c -> new SingleResponse(c))
        );
    }
}
