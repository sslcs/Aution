package com.happy.auction.entity.param;

/**
 * Created by LiuCongshan on 17-9-26.
 * 获取竞拍记录参数
 */

public class BidRecordParam extends BaseParam {
    public int sid;
    public int start;
    public int limit = DEFAULT_LIMIT;

    public BidRecordParam() {
        action = "goods_bid_records";
    }
}
