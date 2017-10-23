package com.happy.auction.entity.param;

/**
 * 获取最新成交参数<br/>
 * Created by LiuCongshan on 17-10-23.
 *
 * @author LiuCongshan
 */

public class BaskParam extends BaseParam {
    public int start;
    public int limit = DEFAULT_LIMIT;

    public BaskParam() {
        action = "bask_my";
    }
}
