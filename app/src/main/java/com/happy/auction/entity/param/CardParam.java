package com.happy.auction.entity.param;

/**
 * 我的卡密请求参数<br/>
 * Created by LiuCongshan on 17-9-11.
 *
 * @author LiuCongshan
 */

public class CardParam extends BaseParam {
    public int start;
    public int limit = DEFAULT_LIMIT;

    public CardParam() {
        action = "prize_get_card_info_list";
    }
}
