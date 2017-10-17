package com.happy.auction.module.address;

import java.util.ArrayList;

/**
 * Created by LiuCongshan on 17-10-17.<br/>
 * xml中地址记录项
 *
 * @author LiuCongshan
 */

public class AddressRecord {
    public String name;
    public String aid;

    public ArrayList<AddressRecord> child;

    public ArrayList<AddressRecord> getChild() {
        if (child == null) {
            child = new ArrayList<>();
        }
        return child;
    }

    @Override
    public String toString() {
        return name;
    }
}
