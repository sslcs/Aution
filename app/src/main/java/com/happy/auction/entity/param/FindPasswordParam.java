package com.happy.auction.entity.param;

/**
 * 找回密码参数
 *
 * @author LiuCongshan
 * @date 17-10-31
 */

public class FindPasswordParam extends BaseParam {
    /**
     * 手机号
     */
    public String phone;
    /**
     * 新密码
     */
    public String pwd;
    /**
     * 验证码
     */
    public String code;

    public FindPasswordParam() {
        action = "user_findpwd";
    }
}
