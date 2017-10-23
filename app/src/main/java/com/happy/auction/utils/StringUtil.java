package com.happy.auction.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LiuCongshan on 17-9-20.<br/>
 * 字符串相关工具类
 * @see #formatMoney(int)
 * <li>formatMoney</li>
 * <li>formatSignMoney</li>
 * <li>formatDateTime</li>
 * <li>formatTimeMinute</li>
 * <li>formatTimeMinute</li>
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

    public static String formatTimeMinute(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        return format.format(date);
    }

    public static String formatPercent(int save) {
        float percent = save / 100.0f;
        return String.format(Locale.CHINA, "%.2f%%", percent);
    }
}
