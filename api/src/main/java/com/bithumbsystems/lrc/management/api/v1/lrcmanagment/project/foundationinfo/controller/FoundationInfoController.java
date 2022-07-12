package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.controller;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.model.request.FoundationInfoRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.service.FoundationInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/lrcmanagment/project")
@Tag(name = "재단 정보", description = "재단 정보 API")
public class FoundationInfoController {

    private final FoundationInfoService foundationInfoService;

    /**
     * 재단 정보 찾기
     * @param id
     * @return FoundationInfoResponse Object
     */
    @GetMapping("/foundation-info/{id}")
    @Operation(summary = "거래지원 관리 - 재단 정보 조회", description = "projectId를 이용하여 project를 조회합니다.", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 검색")
    public ResponseEntity<Mono<?>> getFoundationInfo(@Parameter(name = "id", description = "project 의 id", in = ParameterIn.PATH)
                                                     @PathVariable("id") String id) {
        return ResponseEntity.ok().body(foundationInfoService.findById(id)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 프로젝트 정보 업데이트
     * @param id
     * @param foundationInfoRequest
     * @return FoundationInfoResponse Object
     */
    @PostMapping("/foundation-info/{id}")
    @Operation(summary = "거래지원 관리 - 재단 정보 업데이트", description = "projectId를 이용하여 project를 수정 합니다.", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 수정")
    public ResponseEntity<Mono<?>> updateFoundationInfo(@Parameter(name = "projectId", description = "project 의 id", in = ParameterIn.PATH)
                                                            @PathVariable("id") String id,
                                                        @RequestBody FoundationInfoRequest foundationInfoRequest,
                                                        @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(foundationInfoService.updateFoundationInfo(id, foundationInfoRequest, account)
                .map(c -> new SingleResponse(c))
        );
    }
}
