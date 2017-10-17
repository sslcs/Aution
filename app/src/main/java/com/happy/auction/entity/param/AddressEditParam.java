package com.happy.auction.entity.param;

/**
 * Created by LiuCongshan on 17-10-17.<br/>
 * 编辑收货地址参数
 */

public class AddressEditParam extends AddressAddParam {
    /**
     * id
     */
    public String aid;

    public AddressEditParam() {
        action = "user_address_edit";
    }
}
