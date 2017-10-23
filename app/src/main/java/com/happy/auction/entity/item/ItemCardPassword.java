package com.happy.auction.entity.item;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * 卡号密码item<br/>
 * Created by LiuCongshan on 17-10-20.
 *
 * @author LiuCongshan
 */

public class ItemCardPassword {
    public boolean isSelected;
    public boolean isHidden = true;
    /**
     * 卡号
     */
    @SerializedName("cardno")
    public String cardNumber;
    /**
     * 密码
     */
    @SerializedName("cardpw")
    public String cardPassword;

    @Override
    public String toString() {
        if (TextUtils.isEmpty(cardNumber) || TextUtils.isEmpty(cardPassword)) {
            return "";
        }
        return cardNumber + "," + cardPassword + "/";
    }
}
