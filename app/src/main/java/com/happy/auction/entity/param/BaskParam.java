package com.happy.auction.entity.param;

import java.util.ArrayList;

/**
 * 新增晒单参数
 *
 * @author LiuCongshan
 * @date 17-10-25
 */

public class BaskParam extends BaseParam {
    /**
     * 对应商品编号id
     */
    public int sid;
    /**
     * 评论内容
     */
    public String content;
    /**
     * 晒单图片
     */
    public ArrayList<String> images;

    public BaskParam() {
        action = "bask_new";
    }
}
