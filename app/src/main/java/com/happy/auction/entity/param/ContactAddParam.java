package com.happy.auction.entity.param;

/**
 * Created by LiuCongshan on 17-10-17.<br/>
 * 添加联系人参数
 */

public class ContactAddParam extends BaseParam{
    /**
     * 是否默认， 1:是；0:否
     */
    public int is_default = 0;
    /**
     * 类型 1: phone, 2: qq
     */
    public int vir_addr_type = 1;
    /**
     * 备注
     */
    public String remark;
    /**
     * 手机号
     */
    public String phone;
    /**
     * QQ号
     */
    public String qq;

    public ContactAddParam() {
        action = "user_vir_address_add";
    }
}
