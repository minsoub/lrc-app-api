package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.user.exception;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type User exception.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserException extends RuntimeException {
  private final ErrorCode errorCode;

  /**
   * Instantiates a new User exception.
   *
   * @param errorCode the error code
   */
  public UserException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
