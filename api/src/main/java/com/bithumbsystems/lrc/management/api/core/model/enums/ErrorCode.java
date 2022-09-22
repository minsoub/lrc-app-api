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
  INVALID_DATE_MONTH_AFTER("F007", "It's up to 3 months."),
  INVALID_DATE_DAY_PREVIOUS("F008", "It's bigger than the previous date"),
  INVALID_MAX_FILE_SIZE("F016","INVALID_MAX_FILE_SIZE"),
  INVALID_FILE_EXT("F017","INVALID_FILE_EXT"),
  INVALID_NUMBER_FORMAT("N004","You can enter up to 4 decimal places."),
  FAIL_SEND_MAIL("M411","FAIL_SEND_MAIL");


  private final String code;

  private final String message;
}
