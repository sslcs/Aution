package com.happy.auction.entity.param;

/**
 * Created by LiuCongshan on 17-9-26.
 * 获取往期成交参数
 */

public class PreviousParam extends BaseParam {
    public int gid;
    public int start;
    public int limit = DEFAULT_LIMIT;

    public PreviousParam() {
        action = "goods_prize_history";
    }
}
