package com.happy.auction.base;

import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

/**
 * Activity基类，统计页面时长
 *
 * @author LiuCongshan
 */
public abstract class BaseTimeActivity extends AppCompatActivity {
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public int getColorCompat(@ColorRes int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColor(resId, getTheme());
        } else {
            return getResources().getColor(resId);
        }
    }
}
