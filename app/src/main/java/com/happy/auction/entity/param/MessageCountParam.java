package com.happy.auction.entity.param;

/**
 * 获取未读消息数目参数
 *
 * @author LiuCongshan
 * @date 17-9-11
 */

public class MessageCountParam extends BaseParam {
    public MessageCountParam() {
        action = "message_ucount";
    }
}
