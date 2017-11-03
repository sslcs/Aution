package com.happy.auction.base;

import com.umeng.analytics.MobclickAgent;

/**
 * Activity基类，统计页面跳转
 *
 * @author LiuCongshan
 */
public abstract class BasePageActivity extends BaseTimeActivity {
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }
}
