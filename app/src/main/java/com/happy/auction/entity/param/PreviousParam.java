package com.happy.auction.entity.param;

/**
 * 获取往期成交参数
 *
 * @author LiuCongshan
 * @date 17-9-26
 */

public class PreviousParam extends BaseParam {
    public int gid;
    public int start;
    public int limit = DEFAULT_LIMIT;

    public PreviousParam() {
        action = "goods_prize_history";
    }
}
