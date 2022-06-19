package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.model.request.ProjectInfoRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.service.ProjectInfoService;
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
@Tag(name = "프로젝트 정보", description = "프로젝트 정보 API")
public class ProjectInfoController {

    private final ProjectInfoService projectInfoService;

    /**
     * 프로젝트 정보 1개 id 찾기
     * @param projectId
     * @return ProjectInfoResponse Object
     */
    @GetMapping("/project-info/{projectId}")
    @Operation(summary = "프로젝트 정보 1개 id 찾기", description = "projectId를 이용하여 프로젝트 정보를 조회합니다.")
    public ResponseEntity<Mono<?>> getProjectInfo(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH)
                                                      @PathVariable("projectId") String projectId) {
        return ResponseEntity.ok().body(projectInfoService.findByProjectId(projectId)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 프로젝트 정보 1개 저장
     * @param projectInfoRequest
     * @return ProjectInfoResponse Object
     */
    @PostMapping("/project-info")
    @Operation(summary = "프로젝트 정보 1개 저장", description = "프로젝트 정보를 저장합니다.")
    public ResponseEntity<Mono<?>> createProjectInfo(@Parameter(name = "project Object", description = "프로젝트 의 모든 정보", in = ParameterIn.PATH)
                                                         @RequestBody ProjectInfoRequest projectInfoRequest) {
        return ResponseEntity.ok().body(projectInfoService.create(projectInfoRequest)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 프로젝트 정보 업데이트
     * @param projectId
     * @param projectInfoRequest
     * @return ProjectInfoResponse Object
     */
    @PutMapping("/project-info/{projectId}")
    @Operation(summary = "프로젝트 정보 업데이트", description = "프로젝트 정보를 수정하여 저장합니다.")
    public ResponseEntity<Mono<?>> updateProjectInfo(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH)
                                                         @PathVariable("projectId") String projectId,
                                                     @RequestBody ProjectInfoRequest projectInfoRequest) {
        return ResponseEntity.ok().body(projectInfoService.updateProjectInfo(projectId, projectInfoRequest)
                .map(c -> new SingleResponse(c))
        );
    }
}
