package com.happy.auction.utils;

import java.util.regex.Pattern;

/**
 * 校验工具类
 *
 * @author LiuCongshan
 * @date 17-9-8
 */

public class Validation {
    /**
     * 匹配手机号码
     */
    private static final Pattern PHONE = Pattern.compile("(^(13|15|18|14|17|19)\\d{9}$)");


    /**
     * 密码必须是8～20位数字或字母组成 <br/>
     * \w!"#$%&'()*+,./:;<=>?@\[\]^_`{|}~
     */
    private static final Pattern PASSWORD = Pattern.compile("^[\\w!\"#$%&'()*+,./:;<=>?@\\[\\]^_`{|}~]{8,20}$");

    /**
     * 匹配手机号码
     *
     * @param phone 手机号码
     * @return 是否匹配
     */
    public static boolean phone(CharSequence phone) {
        return PHONE.matcher(phone).matches();
    }

    /**
     * 匹配 8～20位的密码
     *
     * @param password 密码
     * @return 是否匹配
     */
    public static boolean password(CharSequence password) {
        int length = password.length();
        return length > 7 && length < 21;
//        return PASSWORD.matcher(password).matches();
    }
}
