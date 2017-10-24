package com.happy.auction.entity.param;

/**
 * 订单记录请求参数<br/>
 * Created by LiuCongshan on 17-9-11.
 *
 * @author LiuCongshan
 */

public class MessageParam extends BaseParam {
    /**
     * 消息类型， 1 竞拍消息， 2 物流信息， 3 系统公告， 不传时为获取所有
     */
    public int type;
    public int start;
    public int limit = DEFAULT_LIMIT;

    public MessageParam() {
        action = "message_list";
    }
}
