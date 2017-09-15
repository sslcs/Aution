package com.happy.auction.entity.response;

import com.happy.auction.entity.item.ItemGoods;

import java.util.ArrayList;

/**
 * Created by LiuCongshan on 17-9-14.
 * 商品请求返回数据
 */

public class GoodsResponse {
    /**
     * 标签<br/>
     * hot 热拍； green_hand 新手推荐
     */
    public String type;
    /**
     * 分类<br/>
     * 1电脑数码， 2卡券充值 3家用电器 4美食天地 5 日用百搭 6其他
     */
    public String tid;

    public ArrayList<ItemGoods> goods;
}
