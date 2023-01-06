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
  @Schema(description = "계정 상태", allowableValues = {"NORMAL", "DENY_ACCESS", "EMAIL_VALID"})
  private String status;
  @Schema(description = "계정 상태명", allowableValues = {"정상", "로그인 차단", "이메일 인증 중"})
  private String statusName;
  @Schema(description = "생성 프로젝트 (a, b)")
  private String createProjectsName;
  @Schema(description = "참여 프로젝트 (a, b)")
  private String joinProjectsName;
  @Schema(description = "등록일시 (yyyy-MM-dd hh:mm)")
  private LocalDateTime createDate;
  @Schema(description = "최근 접속일시 (yyyy-MM-dd hh:mm)")
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
   * Sets create projects name.
   *
   * @param createProjectsName the create projects name
   */
  public void setCreateProjectsName(String createProjectsName) {
    this.createProjectsName = createProjectsName;
  }

  /**
   * Sets join projects name.
   *
   * @param joinProjectsName the join projects name
   */
  public void setJoinProjectsName(String joinProjectsName) {
    this.joinProjectsName = joinProjectsName;
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
