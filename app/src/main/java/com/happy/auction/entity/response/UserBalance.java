package com.happy.auction.entity.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LiuCongshan on 17-9-29.
 * 用户余额类
 */

public class UserBalance {
    /**
     * 拍币
     */
    @SerializedName("coin")
    public int auction_coin;
    /**
     * 赠币
     */
    @SerializedName("gift_coin")
    public int free_coin;
}
