package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.model.request.FoundationInfoRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.service.FoundationInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/lrcmanagment/project")
public class FoundationInfoController {

    private final FoundationInfoService foundationInfoService;

    /**
     * 재단 정보 찾기
     * @param projectId
     * @return FoundationInfoResponse Object
     */
    @GetMapping("/foundation-info/{projectId}")
    public ResponseEntity<Mono<?>> getFoundationInfo(@PathVariable("projectId") String projectId) {
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
    public ResponseEntity<Mono<?>> updateFoundationInfo(@PathVariable("projectId") String projectId,
                                                     @RequestBody FoundationInfoRequest foundationInfoRequest) {
        return ResponseEntity.ok().body(foundationInfoService.updateFoundationInfo(projectId, foundationInfoRequest)
                .map(c -> new SingleResponse(c))
        );
    }

}
