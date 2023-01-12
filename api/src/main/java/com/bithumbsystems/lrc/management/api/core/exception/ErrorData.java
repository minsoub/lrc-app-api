package com.bithumbsystems.lrc.management.api.core.exception;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import lombok.Getter;

 /**
 * The type Error data.
 */
@Getter
public class ErrorData {
  private final String code;
  private final String message;

  /**
   * Instantiates a new Error data.
   *
   * @param errorCode the error code
   */
  public ErrorData(ErrorCode errorCode) {
    this.code = errorCode.getCode();
    this.message = errorCode.getMessage();
  }

  /**
   * Instantiates a new Error data.
   *
   * @param code    the code
   * @param message the message
   */
  public ErrorData(String code, String message) {
    this.code = code;
    this.message = message;
  }
}