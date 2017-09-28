package com.happy.auction.entity.response;

import com.happy.auction.entity.item.ItemCoin;

import java.util.ArrayList;

/**
 * Created by LiuCongshan on 17-9-28.
 * 金币请求返回数据
 */

public class CoinResponse {
    /**
     * 拍币余额
     */
    public int coin;
    /**
     * 赠币余额
     */
    public int gift_coin;

    public ArrayList<ItemCoin> records;
}
