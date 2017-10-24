package com.happy.auction.entity.param;

/**
 * 获取消息详情参数<br/>
 * Created by LiuCongshan on 17-9-11.
 *
 * @author LiuCongshan
 */

public class MessageDetailParam extends BaseParam {
    public int mid;

    public MessageDetailParam() {
        action = "message_detail";
    }
}
