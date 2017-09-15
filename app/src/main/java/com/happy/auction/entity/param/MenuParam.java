package com.happy.auction.entity.param;

import com.happy.auction.entity.response.BaseEvent;

/**
 * Created by LiuCongshan on 17-9-11.
 * 获取首页banner下面的四个按钮模块配置参数
 */

public class MenuParam extends BaseParam {
    public MenuParam() {
        action = BaseEvent.RESPONSE_MENU;
    }
}
