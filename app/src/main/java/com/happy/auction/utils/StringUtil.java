package com.happy.auction.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LiuCongshan on 17-9-20.
 * 字符串相关工具类
 */

public class StringUtil {
    public static String formatMoney(int money) {
        float fMoney = money / 100.0f;
        return String.format(Locale.CHINA, "%.2f", fMoney);
    }

    public static String formatSignMoney(int money) {
        float fMoney = money / 100.0f;
        return String.format(Locale.CHINA, "￥%.2f", fMoney);
    }

    public static String formatDateTime(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.format(date);
    }

    public static String formatPercent(int percent) {
        float fPercent = percent / 100f;
        return String.format(Locale.CHINA, "%.2f%%", fPercent);
    }
}
