package com.happy.auction.entity.param;

/**
 * 获取往期成交参数
 *
 * @author LiuCongshan
 * @date 17-11-27
 */

public class TrendParam extends BaseParam {
    public int gid;
    public int start;
    public int limit = 50;

    public TrendParam() {
        action = "goods_trend";
    }
}
