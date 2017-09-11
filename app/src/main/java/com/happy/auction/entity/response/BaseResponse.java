package com.happy.auction.entity.response;

/**
 * Created by LiuCongshan on 17-8-21.
 * 接收数据基类
 */

public class BaseResponse extends BaseEvent {
    /**
     * 状态码: 0为正常， 其他为各种错误,
     */
    public int code;

    /**
     * 错误状态下的message
     */
    public String msg;

    public boolean isSuccess() {
        return 0 == code;
    }
}
