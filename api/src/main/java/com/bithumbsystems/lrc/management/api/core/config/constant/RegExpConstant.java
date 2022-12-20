package com.bithumbsystems.lrc.management.api.core.config.constant;

public class RegExpConstant {
  public static final String HTTPS_REG_EXP = "^(https):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/?([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$";

  public static final String EMAIL_REG_EXP = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
}
