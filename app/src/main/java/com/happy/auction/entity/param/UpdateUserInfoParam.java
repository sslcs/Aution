package com.happy.auction.entity.param;

/**
 * Created by LiuCongshan on 17-10-16.
 * 更新用户信息参数
 */

public class UpdateUserInfoParam extends BaseParam {
    public String username;
    public String headimg;

    public UpdateUserInfoParam() {
        action = "user_info_update";
    }
}
