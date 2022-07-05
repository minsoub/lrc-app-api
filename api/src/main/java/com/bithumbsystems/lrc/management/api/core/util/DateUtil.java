package com.bithumbsystems.lrc.management.api.core.util;

import java.time.LocalDate;

public class DateUtil {

    /**
     * 이전 날짜와 다음 날짜 비교
     * @param fromDate - 이전날짜
     * @param toDate - 다음 날짜
     * @return Boolean
     */
    public static Boolean isAfter(LocalDate fromDate, LocalDate toDate) {
        return fromDate.isAfter(toDate);
    }

    /**
     * 이전 날짜 기준으로 월을 더하여 다음 날짜 비교
     * @param fromDate - 이전날짜
     * @param toDate - 다음 날짜
     * @param month - 월
     * @return Boolean
     */
    public static Boolean isBetterThenPrevious(LocalDate fromDate, LocalDate toDate, Integer month) {
        return fromDate.plusMonths(month).isBefore(toDate);
    }
}
