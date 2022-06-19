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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
}
