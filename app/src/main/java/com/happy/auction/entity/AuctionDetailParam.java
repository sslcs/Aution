package com.happy.auction.entity;

import com.happy.auction.utils.GsonSingleton;

/**
 * Created by LiuCongshan on 17-8-21.
 * 获取竞拍详情参数
 */

public class AuctionDetailParam {
    public String uid;
    public String sid;

    @Override
    public String toString() {
        return GsonSingleton.get().toJson(this);
    }
}
