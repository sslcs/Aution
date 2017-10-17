package com.happy.auction.entity.param;

/**
 * Created by LiuCongshan on 17-10-17.<br/>
 * 设置默认收货地址参数
 */

public class AddressDefaultParam extends BaseParam {
    /**
     * id
     */
    public int aid;

    public AddressDefaultParam() {
        action = "user_address_default";
    }
}
