package com.happy.auction.entity;

/**
 * Created by LiuCongshan on 17-8-21.
 * 请求参数基类
 */

public class BaseRequest<T> {
    public String action;

    public T data;
}
