package com.happy.auction.entity.param;

import com.happy.auction.entity.response.BaseEvent;

/**
 * Created by LiuCongshan on 17-9-11.
 * 获取首页banner下面的四个按钮模块配置参数
 */

public class AnnounceParam extends BaseParam {
    public AnnounceParam() {
        action = BaseEvent.RESPONSE_ANNOUNCE;
    }
}
