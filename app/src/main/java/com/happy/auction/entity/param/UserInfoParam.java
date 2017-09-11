package com.happy.auction.entity.param;

import com.happy.auction.entity.response.BaseEvent;

/**
 * Created by LiuCongshan on 17-9-11.
 * 获取用户信息参数
 */

public class UserInfoParam extends BaseParam {
    public UserInfoParam() {
        action = BaseEvent.RESPONSE_USER_INFO;
    }
}
