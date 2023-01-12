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
  INVALID_PROJECT("F017","INVALID_PROJECT"),
  INVALID_NUMBER_FORMAT("N004","You can enter up to 4 decimal places."),
  FAIL_SEND_MAIL("M411","FAIL_SEND_MAIL"),
  PROJECT_NAME_DUPLICATE("F029", "PROJECT_NAME_DUPLICATE"),
   INVALID_CHAT_ROOM("C001", "CHAT_ROOM is invalid"),
  FAIL_VALIDATE_PARAMETER("F030", "FAIL_VALIDATE_PARAMETER"),
  FAIL_PARSING_PARAMETER("F031", "FAIL_PARSING_PARAMETER");

  private final String code;

  private final String message;
}
