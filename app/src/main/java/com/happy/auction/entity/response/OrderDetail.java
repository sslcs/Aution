package com.happy.auction.entity.response;

import com.happy.auction.entity.item.Address;
import com.happy.auction.entity.item.AddressVirtual;
import com.happy.auction.entity.item.ItemOrder;

/**
 * Created by LiuCongshan on 17-8-18.
 * 订单详情
 */

public class OrderDetail extends ItemOrder {
    /**
     * 商品类型 id
     */
    public int tid;
    /**
     * 拍品类型，1: 实物，2: 虚拟直充，3：虚拟卡密
     */
    public int type;
    /**
     * 拍中后付款时间，单位：毫秒
     */
    public long pay_time;
    /**
     * 拍中后确认领奖时间，单位：毫秒
     */
    public long confirm_prize_time;
    /**
     * 拍中后晒单时间，单位：毫秒
     */
    public long bask_time;
    /**
     * status==4、type!=1 时为确认收货的虚拟收货地址
     */
    public AddressVirtual vir_address;
    /**
     * status==3、type==1 时为用户默认实物收货地址，status==4、type==1 时为确认收货的实物收货地址
     */
    public Address address;

    public OrderDetail(ItemOrder item) {
        if (item == null) return;

        pid = item.pid;
        order_num = item.order_num;
        buy = item.buy;
        refund_coin = item.refund_coin;
        prize_time = item.prize_time;
        status = item.status;
    }

    public String getAddress() {
        if (address == null) return "";
        if (address.province == null)
            return "a;sjdfl;asjdlfjaslfjlasjlfjasfjashkfhakshfkhaskfhkasdhfkhskdhfkshadkhsa";
        return address.province + address.city + address.district + address.street;
    }

    public String getPhone() {
        if (address == null) return "";if (address.province == null)
            return "12345678910";
        return address.phone;
    }

    public String getUsername() {
        if (address == null) return "";if (address.province == null)
            return "恭喜发财";
        return address.username;
    }

    public boolean showAddress() {
        return type == 1 && status > 2;
    }
}
