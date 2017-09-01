package com.happy.auction.entity;

/**
 * Created by LiuCongshan on 17-8-21.
 */

public class BaseResponse<T extends  BaseEvent> {
    public int code;

    public T data;
}
