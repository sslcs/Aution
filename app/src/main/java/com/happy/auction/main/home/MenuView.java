package com.happy.auction.main.home;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.happy.auction.entity.item.ItemMenu;

import java.util.ArrayList;

/**
 * Created by LiuCongshan on 17-9-13.
 */

public class MenuView extends ConstraintLayout {
    private ArrayList<ItemMenu> data;

    public MenuView(Context context) {
        super(context);
    }

    public MenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(ArrayList<ItemMenu> data) {
        this.data = data;
        if (data == null || data.isEmpty()) return;
        int length = data.size();
        ConstraintLayout.LayoutParams ivParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        ConstraintLayout.LayoutParams tvParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < length; i++) {
            ItemMenu menu = data.get(i);
            ImageView iv = new ImageView(getContext());
            LayoutParams iParams = new LayoutParams(ivParams);
            iParams.topToTop = LayoutParams.PARENT_ID;
            iParams.startToStart = i == 0 ? LayoutParams.PARENT_ID : 0xfe + i - 1;
            iParams.endToStart = i == length - 1 ? LayoutParams.PARENT_ID : 0xfe + i + 1;
            iv.setId(0xfe + i);
            addView(iv, iParams);

            TextView tv = new TextView(getContext());
            LayoutParams tParams = new LayoutParams(tvParams);
            tParams.topToTop = 0;
            tParams.bottomToBottom = LayoutParams.PARENT_ID;
            tParams.startToStart = i == 0 ? LayoutParams.PARENT_ID : 0xff + i - 1;
            tParams.endToStart = i == length - 1 ? LayoutParams.PARENT_ID : 0xff + i + 1;
            tv.setId(0xff + i);
            tv.setText(menu.title);
            addView(tv, tParams);
        }
    }
}
