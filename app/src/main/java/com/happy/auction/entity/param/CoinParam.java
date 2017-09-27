package com.happy.auction.entity.param;

/**
 * Created by LiuCongshan on 17-9-27.
 * 获取我的账户记录参数
 */

public class CoinParam extends BaseParam {
    /**
     * 最后一条充值记录 id
     */
    public int start;
    public int limit;
    /**
     * 币的类型，1 为拍币，2 为赠币
     */
    public int coin_type;


    public CoinParam() {
        action = "charge_my_records";
    }
}
