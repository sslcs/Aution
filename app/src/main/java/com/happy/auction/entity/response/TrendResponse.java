package com.happy.auction.entity.response;

import com.happy.auction.entity.item.ItemTrend;

import java.util.ArrayList;

/**
 * 成交价趋势请求返回数据
 *
 * @author LiuCongshan
 * @date 17-11-27
 */

public class TrendResponse {
    /**
     * 拍币余额
     */
    public int min_price;
    /**
     * 赠币余额
     */
    public int max_price;

    public ArrayList<ItemTrend> trend_data;
}
