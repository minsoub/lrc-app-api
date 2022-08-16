package com.bithumbsystems.lrc.management.api.core.util;

import java.text.DecimalFormat;

public class FormatUtil {
    public static String formatCurrency(Double amount) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0.0000");
        if (amount != null)
            return formatter.format(amount); // Double.parseDouble(amount));
        else
            return "";
    }
}
