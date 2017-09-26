package com.happy.auction.entity.param;

/**
 * Created by LiuCongshan on 17-9-26.
 * 获取最新成交参数
 */

public class LatestParam extends BaseParam {
    public int start;
    public int limit = DEFAULT_LIMIT;

    public LatestParam() {
        action = "prize_latest";
    }
}
