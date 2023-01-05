package com.bithumbsystems.persistence.mongodb.lrcmanagment.user.model.enums;

import lombok.Getter;

/**
 * The enum User status.
 */
@Getter
public enum UserStatus {
  /**
   * Normal user status.
   */
  NORMAL("정상"),
  /**
   * Deny access user status.
   */
  DENY_ACCESS("로그인 차단"),
  /**
   * Email valid user status.
   */
  EMAIL_VALID("이메일 인증 중");

  private String codeName;

  UserStatus(String codeName) {
    this.codeName = codeName;
  }
}
