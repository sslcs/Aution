package com.happy.auction.entity.param;

/**
 * Created by LiuCongshan on 17-10-17.<br/>
 * 删除收货地址参数
 */

public class AddressDelParam extends BaseParam {
    /**
     * id
     */
    public int aid;

    public AddressDelParam() {
        action = "user_address_del";
    }
}
