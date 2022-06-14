package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.model.request.ProjectInfoRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.service.ProjectInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/lrcmanagment")
public class ProjectInfoController {

    private final ProjectInfoService projectInfoService;

    /**
     * 프로젝트 정보 1개 id 찾기
     * @param projectId
     * @return ProjectInfoResponse Object
     */
    @GetMapping("/project-info/{projectId}")
    public ResponseEntity<Mono<?>> getProjectInfo(@PathVariable("projectId") String projectId) {
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
    public ResponseEntity<Mono<?>> createProjectInfo(@RequestBody ProjectInfoRequest projectInfoRequest) {
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
    public ResponseEntity<Mono<?>> updateProjectInfo(@PathVariable("projectId") String projectId,
                                                     @RequestBody ProjectInfoRequest projectInfoRequest) {
        return ResponseEntity.ok().body(projectInfoService.updateProjectInfo(projectId, projectInfoRequest)
                .map(c -> new SingleResponse(c))
        );
    }
}
