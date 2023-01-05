package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.user.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

/**
 * The type User response.
 */
@Builder
@Getter
public class UserResponse {
  @Schema(description = "사용자 ID")
  private String id;
  @Schema(description = "계정 ID")
  private String email;
  @Schema(description = "계정 상태")
  private String status;
  @Schema(description = "계정 상태명")
  private String statusName;
  @Schema(description = "생성 프로젝트")
  private String createProject;
  @Schema(description = "참여 프로젝트")
  private String joinProject;
  @Schema(description = "등록일시")
  private LocalDateTime createDate;
  @Schema(description = "최근 접속일시")
  private LocalDateTime lastLoginDate;

  /**
   * Gets create date.
   *
   * @return the creation date
   */
  public LocalDateTime getCreateDate() {
    return createDate == null ? LocalDateTime.of(1900, 1, 1, 0, 0) : createDate;
  }

  /**
   * Sets email.
   *
   * @param email the email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Sets create project.
   *
   * @param createProject the creation project
   */
  public void setCreateProject(String createProject) {
    this.createProject = createProject;
  }

  /**
   * Sets join project.
   *
   * @param joinProject the join project
   */
  public void setJoinProject(String joinProject) {
    this.joinProject = joinProject;
  }

  /**
   * Sets status name.
   *
   * @param statusName the status name
   */
  public void setStatusName(String statusName) {
    this.statusName = statusName;
  }
}
