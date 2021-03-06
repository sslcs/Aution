package com.happy.auction.entity.param;

/**
 * 发送验证码请求参数
 *
 * @author LiuCongshan
 * @date 17-9-11
 */
public class CaptchaParam extends BaseParam {
    public String phone;
    /**
     * 0: 注册， 1:找回密码, 2:绑定手机, 3: 免密登录
     */
    public int forgetPwd;

    public CaptchaParam() {
        action = "captcha_send";
    }
}
