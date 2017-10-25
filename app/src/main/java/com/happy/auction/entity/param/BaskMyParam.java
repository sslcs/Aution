package com.happy.auction.entity.param;

/**
 * 获取我的晒单参数
 *
 * @author LiuCongshan
 * @date 17-10-23
 */

public class BaskMyParam extends BaseParam {
    public int start;
    public int limit = DEFAULT_LIMIT;

    public BaskMyParam() {
        action = "bask_my";
    }
}
