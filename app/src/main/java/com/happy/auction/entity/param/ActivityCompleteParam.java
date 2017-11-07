package com.happy.auction.entity.param;

/**
 * 活动结束参数
 * @author LiuCongshan
 * @date 17-11-6
 */

public class ActivityCompleteParam extends BaseParam {
    /**
     * jpbd竞拍宝典
     */
    public String mission = "jpbd";

    public ActivityCompleteParam() {
        action = "activity_complete";
    }
}
