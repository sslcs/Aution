package com.happy.auction.entity.response;

import com.happy.auction.entity.item.ItemMessage;

/**
 * Created by LiuCongshan on 17-10-24.<br/>
 *
 * @author LiuCongshan
 */

public class MessageDetail {
    /**
     * id
     */
    public int mid;
    /**
     * 标题
     */
    public String title;
    /**
     * 内容
     */
    public String content;
    /**
     * 时间戳， 秒
     */
    public long create_time;

    public MessageDetail(ItemMessage item) {
        mid = item.mid;
        title = item.title;
        create_time = item.create_time;
    }
}
