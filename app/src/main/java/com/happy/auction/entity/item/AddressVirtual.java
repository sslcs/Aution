package com.happy.auction.entity.item;

/**
 * Created by LiuCongshan on 17-10-09.
 * 虚拟收货地址
 */

public class AddressVirtual {
    /**
     * 是否默认， 1:是；0:否
     */
    public int is_default;
    /**
     * 类型 1: phone, 2: qq
     */
    public int vir_addr_type;
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
