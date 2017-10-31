package com.happy.auction.entity.param;

/**
 * 修改密码参数
 *
 * @author LiuCongshan
 * @date 17-10-19
 */

public class ChangePasswordParam extends BaseParam {
    /**
     * 旧密码，可选
     */
    public String old_pwd;
    /**
     * 新密码
     */
    public String pwd;

    public ChangePasswordParam() {
        action = "user_chpwd";
    }
}
