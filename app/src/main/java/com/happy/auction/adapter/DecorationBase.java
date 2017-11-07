package com.happy.auction.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * divider基类
 *
 * @author LiuCongshan
 * @date 17-9-14
 */

public class DecorationBase extends RecyclerView.ItemDecoration {
    private boolean enableHeader = false;
    private boolean enableFooter = false;

    public void enableHeader() {
        enableHeader = true;
    }

    public void enableFooter() {
        enableFooter = true;
    }


    public boolean isEnableHeader() {
        return enableHeader;
    }

    public boolean isEnableFooter() {
        return enableFooter;
    }
}
