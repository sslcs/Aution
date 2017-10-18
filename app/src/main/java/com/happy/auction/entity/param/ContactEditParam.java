package com.happy.auction.entity.param;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LiuCongshan on 17-10-17.<br/>
 * 编辑联系人参数
 */

public class ContactEditParam extends BaseParam {
    /**
     * id
     */
    public int vaid;
    /**
     * 是否默认， 1:是；0:否
     */
    public int is_default = 0;
    /**
     * 类型 1: phone, 2: qq
     */
    @SerializedName("vir_addr_type")
    public int type = 1;
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

    /**
     * 添加或编辑联系人
     *
     * @param isEdit 是否是编辑
     */
    public ContactEditParam(boolean isEdit) {
        if (isEdit) {
            action = "user_vir_address_edit";
        } else {
            action = "user_vir_address_add";
        }
    }
}
