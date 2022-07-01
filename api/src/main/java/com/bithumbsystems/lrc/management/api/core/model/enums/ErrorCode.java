package com.bithumbsystems.lrc.management.api.core.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
  UNKNOWN_ERROR("F001", "error"),
  INVALID_FILE("F002","file is invalid"),
  FAIL_SAVE_FILE("F003","file save fail"),
  INVALID_TOKEN("F004","Invalid token"),

  NOT_FOUND_CONTENT("F004","not found content"),
  FAIL_UPDATE_CONTENT("F005","cannot update content"),
  FAIL_CREATE_CONTENT("F006","cannot create content"),
  INVALID_DATE_AFTER("F007", "It's up to 3 months.");


  private final String code;

  private final String message;
}
