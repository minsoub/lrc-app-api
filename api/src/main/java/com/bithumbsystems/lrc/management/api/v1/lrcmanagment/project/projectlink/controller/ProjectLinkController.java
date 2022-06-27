package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.model.request.ProjectLinkRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.service.ProjectLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/lrcmanagment/project")
@Tag(name = "프로젝트 링크", description = "프로젝트 링크 API")
public class ProjectLinkController {

    private final ProjectLinkService projectLinkService;

    /**
     * 프로젝트 링크 가져오기
     * @param projectId
     * @return FoundationResponse
     */
    @GetMapping("/project-link/link/{projectId}")
    @Operation(summary = "프로젝트 링크 가져오기", description = "프로젝크 링크 정보를 조회 합니다.")
    public ResponseEntity<Mono<?>> getProjectLink(@PathVariable String projectId) {
        return ResponseEntity.ok().body(projectLinkService.findByProjectLinkList(projectId)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 프로젝트 연결 재단 조회
     * @param symbol
     * @return FoundationResponse
     */
    @GetMapping("/project-link/foundation/{symbol}")
    @Operation(summary = "프로젝트 연결 재단 검색", description = "프로젝트 연결 재단을 조회 합니다.")
    public ResponseEntity<Mono<?>> getFoundationList(@PathVariable String symbol) {
        return ResponseEntity.ok().body(projectLinkService.findBySymbolLike(symbol)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 프로젝트 링크 파라미터 가져오기
     * @param projectId
     * @return FoundationResponse
     */
    @GetMapping("/project-link")
    @Operation(hidden = true, summary = "프로젝트 링크 파라미터 가져오기", description = "프로젝크 링크를 파라미터로 조회 합니다.")
    public ResponseEntity<Mono<?>> getProjectLink(@RequestParam String projectId, @RequestParam String linkProjectId) {
        return ResponseEntity.ok().body(projectLinkService.findByLinkProject(projectId, linkProjectId)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 프로젝트 링크 저장
     * @param projectLinkRequest
     * @return FoundationResponse
     */
    @PostMapping("/project-link")
    @Operation(summary = "프로젝트 링크 저장", description = "프로젝크 링크 정보를 저장 합니다.")
    public ResponseEntity<Mono<?>> createProjectLink(@RequestBody ProjectLinkRequest projectLinkRequest) {
        return ResponseEntity.ok().body(projectLinkService.create(projectLinkRequest)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 프로젝트 링크 삭제(링크해제)
     * @param projectLinkRequest
     * @return FoundationResponse
     */
    @DeleteMapping("/project-link")
    @Operation(summary = "프로젝트 링크 삭제(링크해제)", description = "프로젝크 링크 정보를 삭제 합니다.")
    public ResponseEntity<Flux<?>> getProjectLink(@RequestBody ProjectLinkRequest projectLinkRequest) {
        return ResponseEntity.ok().body(projectLinkService.deleteLinkProject(projectLinkRequest)
                .map(c -> new SingleResponse(c))
        );
    }
}
