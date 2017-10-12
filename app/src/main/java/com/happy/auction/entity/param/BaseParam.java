package com.happy.auction.entity.param;

/**
 * Created by LiuCongshan on 17-9-11.
 * 公有临时参数action
 */

public class BaseParam {
    /**
     * 每个分页item的数量
     */
    public final static int DEFAULT_LIMIT = 10;
    /**
     * 接口名称
     */
    transient String action;
}
