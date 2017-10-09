package com.happy.auction.entity.item;

/**
 * Created by LiuCongshan on 17-10-09.
 * 收货地址
 */

public class Address {
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
}
