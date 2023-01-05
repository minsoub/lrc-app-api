package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type User account response.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "사용자 정보")
public class UserAccountResponse {

  private String id;

  @Schema(description = "계정")
  private String userEmail;

  @Schema(description = "프로젝트 코드")
  private String projectId;

  @Schema(description = "회원 아이디")
  private String userAccountId;

  @Schema(description = "회원명")
  private String userName;

  @Schema(description = "마스터/담당자구분")
  private String userType;

  @Schema(description = "SNS ID")
  private String snsId;

  @Schema(description = "Email")
  private String email;

  @Schema(description = "Phone")
  private String phone;

  @Schema(description = "프로젝트명")
  private String projectName;
}
