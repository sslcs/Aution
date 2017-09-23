package com.happy.auction.entity.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LiuCongshan on 17-8-31.
 * User data view model
 */

public class UserInfo {
    public String username;
    public String phone;
    @SerializedName("avatar")
    public String avatar;
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
    /**
     * 积分
     */
    public int points;

    public String uid_unique;

    private int is_pwd_set;

    public boolean noPassword() {
        return is_pwd_set == 0;
    }
}
