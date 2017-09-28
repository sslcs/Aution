package com.happy.auction.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * Created by LiuCongshan on 17-9-14.
 * divider基类
 */

public class DecorationBase extends RecyclerView.ItemDecoration {
    private boolean enableHeader = false;
    private boolean enableFooter = true;

    public void enableHeader() {
        enableHeader = true;
    }

    public void disableFooter() {
        enableFooter = false;
    }


    public boolean isEnableHeader() {
        return enableHeader;
    }

    public boolean isEnableFooter() {
        return enableFooter;
    }
}
