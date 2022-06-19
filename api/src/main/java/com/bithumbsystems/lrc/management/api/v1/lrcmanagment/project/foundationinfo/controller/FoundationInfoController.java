package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.controller;

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
     * @param projectId
     * @return FoundationInfoResponse Object
     */
    @GetMapping("/foundation-info/{projectId}")
    @Operation(summary = "재단 정보 조회", description = "projectId를 이용하여 project를 조회합니다.")
    public ResponseEntity<Mono<?>> getFoundationInfo(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH)
                                                     @PathVariable("projectId") String projectId) {
        return ResponseEntity.ok().body(foundationInfoService.findByProjectId(projectId)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 프로젝트 정보 업데이트
     * @param projectId
     * @param foundationInfoRequest
     * @return FoundationInfoResponse Object
     */
    @PostMapping("/foundation-info/{projectId}")
    @Operation(summary = "재단 정보 업데이트", description = "projectId를 이용하여 project를 수정 합니다.")
    public ResponseEntity<Mono<?>> updateFoundationInfo(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH)
                                                            @PathVariable("projectId") String projectId,
                                                        @RequestBody FoundationInfoRequest foundationInfoRequest) {
        return ResponseEntity.ok().body(foundationInfoService.updateFoundationInfo(projectId, foundationInfoRequest)
                .map(c -> new SingleResponse(c))
        );
    }

}
