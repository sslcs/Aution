package com.happy.auction.entity.param;

/**
 * Created by LiuCongshan on 17-10-17.<br/>
 * 确认地址参数
 */

public class ConfirmAddressParam extends BaseParam {
    /**
     * 商品id
     */
    public int sid;
    /**
     * 地址id
     */
    public int aid;

    public ConfirmAddressParam() {
        action = "prize_confirm";
    }
}
