package com.bithumbsystems.lrc.management.api.foundation.exception;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FoundationException extends RuntimeException {
  public FoundationException(ErrorCode errorCode) {
    super(String.valueOf(errorCode));
  }
}