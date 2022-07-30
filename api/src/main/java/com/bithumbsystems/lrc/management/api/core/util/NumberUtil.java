package com.bithumbsystems.lrc.management.api.core.util;

import java.util.regex.Pattern;

public class NumberUtil {

  public static boolean checkDecimalPoint(Double number) {
    final var pattern = "(^[0-9]+)[.]?\\d{1,4}$";
    return Pattern.matches(pattern, number.toString());
  }
}