package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.controller;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.model.request.ProjectInfoRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.model.response.ProjectInfoResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.service.ProjectInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * The type Project info controller.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/lrcmanagment/project")
@Tag(name = "프로젝트 정보", description = "프로젝트 정보 API")
public class ProjectInfoController {

  private final ProjectInfoService projectInfoService;

  /**
   * 프로젝트 정보 1개 id 찾기.
   *
   * @param projectId the project id
   * @return ProjectInfoResponse Object
   */
  @GetMapping("/project-info/{projectId}")
  @Operation(summary = "거래지원 관리 - 프로젝트 정보 1개 id 찾기", description = "projectId를 이용하여 프로젝트 정보를 조회합니다.",
      tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 프로젝트 정보 > 1개 찾기")
  public ResponseEntity<Mono<SingleResponse<ProjectInfoResponse>>> getProjectInfo(
      @Parameter(description = "project 의 projectId") @PathVariable("projectId") String projectId) {
    return ResponseEntity.ok().body(projectInfoService.findByProjectId(projectId)
        .map(SingleResponse::new)
    );
  }

  /**
   * 프로젝트 정보 1개 저장.
   *
   * @param projectInfoRequest the project info request
   * @return ProjectInfoResponse Object
   */
  @PostMapping("/project-info")
  @Operation(summary = "거래지원 관리 - 프로젝트 정보 1개 저장", description = "프로젝트 정보를 저장합니다.", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 프로젝트 정보 > 1개 저장")
  public ResponseEntity<Mono<SingleResponse<ProjectInfoResponse>>> createProjectInfo(
      @Parameter(name = "project Object", description = "프로젝트 의 모든 정보") @RequestBody ProjectInfoRequest projectInfoRequest) {
    return ResponseEntity.ok().body(projectInfoService.create(projectInfoRequest)
        .map(SingleResponse::new)
    );
  }

  /**
   * 프로젝트 정보 업데이트.
   *
   * @param projectId          the project id
   * @param projectInfoRequest the project info request
   * @param account            the account
   * @return ProjectInfoResponse Object
   */
  @PutMapping("/project-info/{projectId}")
  @Operation(summary = "거래지원 관리 - 프로젝트 정보 업데이트", description = "프로젝트 정보를 수정하여 저장합니다.", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 프로젝트 정보 > 1개 수정")
  public ResponseEntity<Mono<SingleResponse<ProjectInfoResponse>>> updateProjectInfo(
      @Parameter(description = "project 의 projectId") @PathVariable("projectId") String projectId,
      @RequestBody ProjectInfoRequest projectInfoRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(projectInfoService.updateProjectInfo(projectId, projectInfoRequest, account)
        .map(SingleResponse::new)
    );
  }
}
