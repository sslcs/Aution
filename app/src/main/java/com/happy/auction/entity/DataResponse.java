package com.happy.auction.entity;

import com.happy.auction.entity.response.BaseResponse;

/**
 * Created by LiuCongshan on 17-8-21.
 *
 */

public class DataResponse<T> extends BaseResponse {
    /**
     * 正常状态返回的数据
     */
    public T data;
}
