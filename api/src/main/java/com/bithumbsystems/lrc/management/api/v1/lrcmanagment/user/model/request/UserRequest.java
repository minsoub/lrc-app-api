package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.user.model.request;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.user.model.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserRequest {
  @Schema(description = "사용자 ID")
  private String id;

  @NotBlank
  @Pattern(regexp = "^(NORMAL|DENY_ACCESS|EMAIL_VALID)$")
  @Schema(description = "계정 상태", allowableValues = {"NORMAL", "DENY_ACCESS", "EMAIL_VALID"})
  private UserStatus userStatus;
}
