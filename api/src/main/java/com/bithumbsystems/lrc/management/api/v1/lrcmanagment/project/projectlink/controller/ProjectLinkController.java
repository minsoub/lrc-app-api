package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.controller;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.model.request.ProjectLinkRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.service.ProjectLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    @Operation(summary = "거래지원 관리 - 프로젝트 링크 가져오기", description = "프로젝크 링크 정보를 조회 합니다.", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 프로젝트 연결 > 검색")
    public ResponseEntity<Mono<?>> getProjectLink(@PathVariable String projectId) {
        return ResponseEntity.ok().body(projectLinkService.findByProjectLinkList(projectId)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 프로젝트 연결을 위한 재단 조회
     * @param symbol
     * @return FoundationResponse
     */
    @GetMapping("/project-link/foundation")
    @Operation(summary = "거래지원 관리 - 프로젝트 연결을 위한 재단 검색", description = "프로젝트 연결을 위한 재단을 조회 합니다.", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 프로젝트 연결 > 재단 검색")
    public ResponseEntity<Mono<?>> getFoundationList(@RequestParam String symbol, @RequestParam String projectId) {
        return ResponseEntity.ok().body(projectLinkService.findBySymbolLike(symbol, projectId)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 프로젝트 링크 파라미터 가져오기
     * @param projectId
     * @return FoundationResponse
     */
    @GetMapping("/project-link")
    @Operation(hidden = true, summary = "거래지원 관리 - 프로젝트 링크 파라미터 가져오기", description = "프로젝크 링크를 파라미터로 조회 합니다.")
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
    @Operation(summary = "거래지원 관리 - 프로젝트 링크 저장", description = "프로젝크 링크 정보를 저장 합니다.", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 프로젝트 연결 > 링크 저장")
    public ResponseEntity<Mono<?>> createProjectLink(@RequestBody ProjectLinkRequest projectLinkRequest, @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(projectLinkService.create(projectLinkRequest, account)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 프로젝트 링크 삭제(링크해제)
     * @param linkId
     * @return FoundationResponse
     */
    @DeleteMapping("/project-link/{linkId}")
    @Operation(summary = "거래지원 관리 - 프로젝트 링크 삭제(링크해제)", description = "프로젝크 링크 정보를 삭제 합니다.", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 프로젝트 연결 > 링크 삭제")
    public ResponseEntity<Mono<?>> disconnectLink(@PathVariable String linkId, @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(projectLinkService.deleteLinkProject(linkId, account)
                .map(c -> new SingleResponse(c))
        );
    }
}
