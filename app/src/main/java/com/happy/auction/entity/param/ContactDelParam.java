package com.happy.auction.entity.param;

/**
 * Created by LiuCongshan on 17-10-17.<br/>
 * 删除联系人参数
 */

public class ContactDelParam extends BaseParam{
    /**
     * id
     */
    public int vaid;

    public ContactDelParam() {
        action = "user_vir_address_del";
    }
}
