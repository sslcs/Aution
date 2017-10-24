package com.happy.auction.entity.item;

import java.io.Serializable;

/**
 * Created by LiuCongshan on 17-10-24.<br/>
 *
 * @author LiuCongshan
 */

public class ItemMessage implements Serializable {
    /**
     * id
     */
    public int mid;
    /**
     * 标题
     */
    public String title;
    /**
     * 类型
     */
    public int type;
    /**
     * 时间戳， 秒
     */
    public long create_time;
    /**
     * 是否已读， 0 未读， 1 已读
     */
    public int is_read;
}
