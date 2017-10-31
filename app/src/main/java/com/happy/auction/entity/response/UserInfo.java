package com.happy.auction.entity.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LiuCongshan on 17-8-31.
 * User data view model
 */

public class UserInfo {
    public String username;
    public String phone;
    @SerializedName("headimg")
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
    /**
     * 待付款订单数量
     */
    @SerializedName("unpay_amount")
    public int amountUnpaid;
    /**
     * 是否有新的卡密 1:有， 0：没有
     */
    @SerializedName("has_card")
    public boolean hasCard;
    /**
     * 是否已设置密码
     */
    private int is_pwd_set;

    public boolean noPassword() {
        return is_pwd_set == 0;
    }
}
