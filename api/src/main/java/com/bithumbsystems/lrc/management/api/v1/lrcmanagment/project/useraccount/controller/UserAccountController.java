package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.model.request.UserAccountRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.service.UserAccountService;
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
@Tag(name = "담당자 정보", description = "담당자 정보 API")
public class UserAccountController {

    private final UserAccountService userAccountService;

    /**
     * 담당자 정보 id로 찾기
     * @param projectId
     * @return ReviewEstimateResponse Object
     */
    @GetMapping("/user-account/{projectId}")
    @Operation(summary = "거래지원 관리 - 담당자 정보 id로 찾기", description = "projectId를 이용하여 담장자 정보를 조회합니다.", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 담당자 정보 > 검색")
    public ResponseEntity<Mono<?>> getUserAccount(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH)
                                                      @PathVariable("projectId") String projectId) {
        return ResponseEntity.ok().body(userAccountService.findByProjectId(projectId)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 담당자 정보 여러개 저장 및 업데이트
     * @param userAccountRequest
     * @return ReviewEstimateResponse Object
     */
    @PostMapping("/user-account/{projectId}")
    @Operation(summary = "거래지원 관리 - 담당자 정보 여러개 저장 및 업데이트", description = "projectId를 이용하여 담장자 정보를 저장/수정합니다.", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 담당자 정보 > 저장 및 삭제")
    public ResponseEntity<Mono<?>> createUserAccount(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH)
                                                         @PathVariable("projectId") String projectId,
                                                     @Parameter(name = "user Object", description = "사용자 정보", in = ParameterIn.PATH)
                                                     @RequestBody UserAccountRequest userAccountRequest) {
        return ResponseEntity.ok().body(userAccountService.create(projectId, userAccountRequest)
                .map(c -> new SingleResponse(c))
        );
    }
}
