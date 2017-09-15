package com.happy.auction.entity.param;

import com.happy.auction.entity.response.BaseEvent;

/**
 * Created by LiuCongshan on 17-9-11.
 * 获取首页banner参数
 */

public class BannerParam extends BaseParam {
    public BannerParam() {
        action = BaseEvent.RESPONSE_BANNER;
    }
}
