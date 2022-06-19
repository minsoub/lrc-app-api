package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.model.request.FoundationRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.service.FoundationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/lrcmanagment/project")
public class FoundationController {

    private final FoundationService foundationService;

    /**
     * 재단 정보 가져오기
     * @return FoundationResponse
     */
    @GetMapping("/foundation")
    public ResponseEntity<Mono<?>> getFoundation() {
        return ResponseEntity.ok().body(foundationService.getFoundation()
                .map(c -> new MultiResponse(c))
        );
    }

    /**
     * 재단정보 1개 저장
     * @param foundationRequest
     * @return FoundationResponse
     */
    @PostMapping("/foundation")
    public ResponseEntity<Mono<?>> createFoundation(@RequestBody FoundationRequest foundationRequest) {
        return ResponseEntity.ok().body(foundationService.create(foundationRequest)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 재단정보 1개 저장
     * @param foundationRequest
     * @return FoundationResponse
     */
    @PostMapping("/foundation/{projectId}")
    public ResponseEntity<Mono<?>> createFoundation1(@PathVariable("projectId") String projectId,
                                                    @RequestBody FoundationRequest foundationRequest) {
        return ResponseEntity.ok().body(foundationService.updateFoundationInfo(projectId, foundationRequest)
                .map(c -> new SingleResponse(c))
        );
    }
}
