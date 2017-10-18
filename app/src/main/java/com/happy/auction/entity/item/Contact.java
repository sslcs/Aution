package com.happy.auction.entity.item;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by LiuCongshan on 17-10-09.
 * 联系人
 */

public class Contact implements Serializable {
    /**
     * id
     */
    public int vaid;
    /**
     * 是否默认， 1:是；0:否
     */
    public int is_default;
    /**
     * 类型 1: phone, 2: qq
     */
    @SerializedName("vir_addr_type")
    public int type;
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
}
