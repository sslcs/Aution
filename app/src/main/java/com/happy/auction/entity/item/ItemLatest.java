package com.happy.auction.entity.item;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.google.gson.annotations.SerializedName;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.utils.StringUtil;

/**
 * Created by LiuCongshan on 17-9-12.
 * 订单item
 */

public class ItemLatest extends BaseGoods {
    /**
     * 订单 id
     */
    public int pid;
    /**
     * 中奖者 uid
     */
    public int uid;
    /**
     * 中奖者用户名
     */
    public String username;
    /**
     * 中奖者头像
     */
    @SerializedName("headimg")
    public String avatar;
    /**
     * 中奖用户节省百分比 * 100
     */
    public int save;
    /**
     * 开奖时间，单位：毫秒
     */
    public long prize_time;

    public SpannableString getDealPrice() {
        String price = StringUtil.formatMoney(current_price);
        String formatted = AppInstance.getInstance().getString(R.string.latest_deal_price, price);
        SpannableString ss = new SpannableString(formatted);
        int color = AppInstance.getInstance().getResources().getColor(R.color.main_red);
        ss.setSpan(new ForegroundColorSpan(color), 5, ss.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
