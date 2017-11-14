package com.happy.auction.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 字符串相关工具类<br/>
 * Created by LiuCongshan on 17-9-20.
 * <li>{@link #formatMoney}</li>
 * <li>{@link #formatSignMoney}</li>
 * <li>{@link #formatDateTime}</li>
 * <li>{@link #formatTimeMinute}</li>
 * <li>{@link #formatTimeMinute}</li>
 *
 * @author LiuCongshan
 */

public class StringUtil {
    public static final String URL_SERVICE_PROTOCOL = "http://106.75.177.248/service_protocol/index.html";
    public static final String URL_SERVICE_CENTER = "http://106.75.177.248/service_center/index.html";

    /**
     * 格式化Money，保留两位小数
     *
     * @param money 单位（分）
     * @return String
     */
    public static String formatMoney(int money) {
        float fMoney = money / 100.0f;
        return String.format(Locale.CHINA, "%.2f", fMoney);
    }

    /**
     * 格式化成带￥符号的Money，保留两位小数
     *
     * @param money 单位（分）
     * @return String
     */
    public static String formatSignMoney(int money) {
        float fMoney = money / 100.0f;
        return String.format(Locale.CHINA, "￥%.2f", fMoney);
    }

    /**
     * 格式化时间（yyyy-MM-dd HH:mm:ss）
     *
     * @param timestamp 单位（毫秒）
     * @return String
     */
    public static String formatDateTime(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.format(date);
    }

    /**
     * 格式化时间（yyyy-MM-dd HH:mm）
     *
     * @param timestamp 单位（毫秒）
     * @return String
     */
    public static String formatTimeMinute(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        return format.format(date);
    }

    /**
     * 格式化百分比，保留两位小数
     *
     * @param save 百分比×100
     * @return String
     */
    public static String formatPercent(int save) {
        float percent = save / 100.0f;
        return String.format(Locale.CHINA, "%.2f%%", percent);
    }

    public static String formatPhone(String phone) {
        if (TextUtils.isEmpty(phone) || phone.length() < 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
}
