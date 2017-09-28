package com.happy.auction.entity.param;

/**
 * Created by LiuCongshan on 17-9-28.
 * 获取商品晒单分享参数
 */

public class DetailBaskParam extends BaseParam {
    public int gid;
    public int start;
    public int limit = DEFAULT_LIMIT;

    public DetailBaskParam() {
        action = "goods_basks";
    }
}
