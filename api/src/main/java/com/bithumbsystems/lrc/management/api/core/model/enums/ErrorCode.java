package com.bithumbsystems.lrc.management.api.core.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
  UNKNOWN_ERROR("error"),
  INVALID_FILE("file is invalid"),
  FAIL_SAVE_FILE("file save fail"),
  NOT_FOUND_CONTENT("not found content"),
  FAIL_UPDATE_CONTENT("cannot update content"),
  FAIL_CREATE_CONTENT("cannot create content");

  private final String message;
}
