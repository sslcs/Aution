package com.happy.auction.entity.item;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 收货地址
 *
 * @author LiuCongshan
 * @date 17-10-09
 */

public class Address implements Serializable {
    /**
     * id
     */
    public int aid;
    /**
     * 是否默认， 1:是；0:否
     */
    public int is_default;
    /**
     * 姓名
     */
    public String username;
    /**
     * 手机号
     */
    public String phone;
    /**
     * 省
     */
    public String province;
    /**
     * 市
     */
    public String city;
    /**
     * 区
     */
    public String district;
    /**
     * 区id
     */
    public int district_aid;
    /**
     * 街道
     */
    public String street;
    /**
     * 邮编
     */
    public String ecode;
    /**
     * 镇
     */
    public String town;

    public String getAddress() {
        return province + city + district + (TextUtils.isEmpty(town) ? "" : town) + street;
    }
}
