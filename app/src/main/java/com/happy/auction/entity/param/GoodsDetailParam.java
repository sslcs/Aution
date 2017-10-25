package com.happy.auction.entity.param;

/**
 * 获取商品详情参数<br/>
 * Created by LiuCongshan on 17-10-24.
 */

public class GoodsDetailParam extends BaseParam {
    public int gid;

    public GoodsDetailParam() {
        action = "goods_html_detail";
    }
}
