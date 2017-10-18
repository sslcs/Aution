package com.happy.auction.entity.param;

/**
 * Created by LiuCongshan on 17-10-17.<br/>
 * 编辑收货地址参数
 */

public class AddressEditParam extends BaseParam {
    /**
     * id
     */
    public int aid;
    /**
     * 姓名
     */
    public String username;
    /**
     * 手机号
     */
    public String phone;
    /**
     * 省
     */
    public String province;
    /**
     * 市
     */
    public String city;
    /**
     * 区
     */
    public String district;
    /**
     * 街道
     */
    public String street;
    /**
     * 镇
     */
    public String town;

    /**
     * 编辑或添加收货地址
     *
     * @param isEdit 是否是编辑
     */
    public AddressEditParam(boolean isEdit) {
        if (isEdit) {
            action = "user_address_edit";
        } else {
            action = "user_address_add";
        }
    }
}
