package com.happy.auction.entity.response;

import com.happy.auction.module.address.AddressRecord;

import java.util.ArrayList;

/**
 * Created by LiuCongshan on 17-9-25.
 * 商品详情出价币数
 */

public class TownResponse extends BaseResponse {
    /**
     * 是否有子地址
     */
    public int has_child;
    /**
     * 乡镇数据
     */
    public ArrayList<AddressRecord> childs;

    public boolean hasChild() {
        return 1 == has_child;
    }
}
