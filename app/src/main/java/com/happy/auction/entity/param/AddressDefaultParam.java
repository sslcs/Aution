package com.happy.auction.entity.param;

/**
 * 设置默认收货地址参数
 *
 * @author LiuCongshan
 * @date 17-10-17
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
