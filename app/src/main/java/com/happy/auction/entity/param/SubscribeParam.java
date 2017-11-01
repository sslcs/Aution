package com.happy.auction.entity.param;

import com.happy.auction.entity.item.ItemGoods;

import java.util.ArrayList;
import java.util.List;

/**
 * 订阅参数
 *
 * @author LiuCongshan
 * @date 17-11-1
 */

public class SubscribeParam extends BaseParam {

    public ArrayList<Integer> sid = new ArrayList<>();

    public SubscribeParam() {
        action = "goods_subscribe";
    }

    public void addAll(List<ItemGoods> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        for (ItemGoods item : list) {
            sid.add(item.sid);
        }
    }
}
