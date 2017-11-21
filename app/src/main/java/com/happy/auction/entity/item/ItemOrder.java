package com.happy.auction.entity.item;

import com.google.gson.annotations.SerializedName;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.utils.StringUtil;

/**
 * 订单item
 *
 * @author LiuCongshan
 * @date 17-9-12
 */

public class ItemOrder extends BaseGoods {
    public final static int STATUS_GOING = 1;
    public final static int STATUS_WIN = 2;
    public final static int STATUS_PAID = 3;
    public final static int STATUS_CONFIRM = 4;
    public final static int STATUS_BASK = 5;
    public final static int STATUS_FINISH = 6;
    /**
     * 订单 id
     */
    public int pid;
    /**
     * 订单号
     */
    @SerializedName("ordernum")
    public String order_num;
    /**
     * 我已出价多少次
     */
    public int buy;
    /**
     * 退币数量
     */
    public int refund_coin;
    /**
     * 开奖时间，单位：毫秒
     */
    public long prize_time;
    /**
     * 订单状态:<br/>
     * 1：正在拍，2：已拍中，3：已付款，4: 已确认领奖方式，5:已晒单，6:已结束
     */
    public int status;

    public String getFormattedPrice() {
        if (status == STATUS_GOING) {
            return AppInstance.getInstance().getString(R.string.format_current_price, StringUtil.formatMoney(current_price));
        } else {
            return AppInstance.getInstance().getString(R.string.format_deal_price, StringUtil.formatMoney(current_price));
        }
    }

    public String getBuyTimes() {
        if (status == STATUS_GOING || status != STATUS_FINISH) {
            return AppInstance.getInstance().getString(R.string.format_bid_times, buy);
        } else {
            return AppInstance.getInstance().getString(R.string.format_bid_refund, buy, refund_coin);
        }
    }
}
