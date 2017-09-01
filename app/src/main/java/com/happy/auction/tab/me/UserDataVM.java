package com.happy.auction.tab.me;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LiuCongshan on 17-8-31.
 * User data view model
 */

public class UserDataVM {
    public String uid;
    public String username;
    public String phone;
    @SerializedName("headimg")
    public String avatar;
    /**
     * 拍币
     */
    public int auction_coin;
    /**
     * 赠币
     */
    public int free_coin;
    /**
     * 积分
     */
    public int point;

}
