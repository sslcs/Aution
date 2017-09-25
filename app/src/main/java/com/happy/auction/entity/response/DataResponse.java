package com.happy.auction.entity.response;

/**
 * Created by LiuCongshan on 17-8-21.
 * 返回数据泛型基类
 */

public class DataResponse<T> extends BaseResponse {
    /**
     * 正常状态返回的数据
     */
    public T data;
}
