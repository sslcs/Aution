package com.happy.auction.entity.param;

/**
 * Created by LiuCongshan on 17-10-17.<br/>
 * 编辑联系人参数
 */

public class ContactEditParam extends ContactAddParam {
    /**
     * id
     */
    public int vaid;

    public ContactEditParam() {
        action = "user_vir_address_edit";
    }
}
