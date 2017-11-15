package com.happy.auction.entity.param;

/**
 * 获取首页banner下面的四个按钮模块配置参数
 *
 * @author LiuCongshan
 * @date 17-9-11
 */

public class GoodsParam extends BaseParam {
    public final static String TYPE_HOT = "hot";
    public final static String TYPE_NEWBIE = "green_hand";
    /**
     * 标签<br/>
     * hot 热拍； green_hand 新手推荐
     */
    public String type;
    /**
     * 分类<br/>
     * 1电脑数码， 2卡券充值 3家用电器 4美食天地 5 日用百搭 6其他
     */
    public int tid;
    public int start;
    public int limit = DEFAULT_LIMIT;

    public GoodsParam() {
        action = "goods_bid";
    }
}
