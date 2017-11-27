package com.happy.auction.entity.item;

/**
 * 趋势图item
 *
 * @author LiuCongshan
 * @date 17-11-24
 */

public class ItemTrend {
    /**
     * 期数
     */
    public String period;

    /**
     * 成交价格，单位：分
     */
    public int deal_price;

    public String getPrice() {
        if (deal_price < 10) {
            return "X.X";
        }

        String price = String.valueOf(deal_price / 100);
        String decimal = String.valueOf(deal_price % 100 / 10);
        return price + "." + decimal;
    }
}
