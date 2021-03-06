package com.happy.auction.entity.param;

/**
 * 订单记录请求参数<br/>
 * Created by LiuCongshan on 17-9-11.
 *
 * @author LiuCongshan
 */

public class OrderParam extends BaseParam {
    /**
     * 记录类型，0：全部，1：正在拍，2：已拍中，3：待付款
     */
    public int record_type;
    public int start;
    public int limit = DEFAULT_LIMIT;

    public OrderParam() {
        action = "purchase_my_records";
    }
}
