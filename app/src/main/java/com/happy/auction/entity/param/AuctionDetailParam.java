package com.happy.auction.entity.param;

/**
 * Created by LiuCongshan on 17-8-21.
 * 获取竞拍详情参数
 */

public class AuctionDetailParam extends BaseParam {
    public int sid;
    public int gid;

    public AuctionDetailParam() {
        action = "goods_detail";
    }
}
