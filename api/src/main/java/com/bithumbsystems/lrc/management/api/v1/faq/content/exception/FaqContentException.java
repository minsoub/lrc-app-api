package com.bithumbsystems.lrc.management.api.v1.faq.content.exception;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FaqContentException extends RuntimeException {
  public FaqContentException(ErrorCode errorCode) {
    super(String.valueOf(errorCode));
  }
}