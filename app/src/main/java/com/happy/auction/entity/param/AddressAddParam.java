package com.happy.auction.entity.param;

/**
 * Created by LiuCongshan on 17-10-17.<br/>
 * 添加收货地址参数
 */

public class AddressAddParam extends BaseParam {
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
     * 镇
     */
    public String town;

    public AddressAddParam() {
        action = "user_address_add";
    }
}
