package com.happy.auction.base;

import android.view.View;

/**
 * 带返回按钮的Activity基类
 *
 * @author LiuCongshan
 */
public abstract class BaseBackActivity extends BasePageActivity {
    public void onClickBack(View view) {
        onBackPressed();
    }
}
