package com.happy.auction.entity.param;

/**
 * Created by LiuCongshan on 17-10-16.
 * 获取Town参数
 */

public class TownParam extends BaseParam {
    public String aid;

    public TownParam() {
        action = "jd_addr_get_child_addr";
    }
}
