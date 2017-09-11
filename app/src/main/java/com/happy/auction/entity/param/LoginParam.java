package com.happy.auction.entity.param;

import com.happy.auction.entity.response.BaseEvent;

/**
 * Created by LiuCongshan on 17-9-11.
 * 登录参数
 */

public class LoginParam extends BaseParam {
    public final static String TYPE_PASSWORD = "1";
    public final static String TYPE_CAPTCHA = "2";
    public String phone;
    /**
     * 验证码, 仅 login_type = 2 时
     */
    public String code;
    /**
     * 密码， 仅 login_type = 1时
     */
    public String pwd;
    /**
     * 1 密码登录， 2 验证码登录
     */
    public String login_type;

    public LoginParam() {
        action = BaseEvent.RESPONSE_LOGIN;
    }
}
