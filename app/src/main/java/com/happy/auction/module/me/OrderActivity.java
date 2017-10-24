package com.happy.auction.module.me;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.ViewPagerAdapter;
import com.happy.auction.base.BaseTabActivity;

/**
 * 订单页面
 *
 * @author LiuCongshan
 */
public class OrderActivity extends BaseTabActivity {
    /**
     * @param selection 选中标签位置
     * @return Intent for this Activity
     */
    public static Intent newIntent(int selection) {
        Intent intent = new Intent(AppInstance.getInstance(), OrderActivity.class);
        intent.putExtra(KEY_SELECTION, selection);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLayout() {
        mBinding.tvToolbarTitle.setText(R.string.order);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.add(OrderFragment.newInstance(OrderFragment.TYPE_ALL));
        adapter.add(OrderFragment.newInstance(OrderFragment.TYPE_GOING));
        adapter.add(OrderFragment.newInstance(OrderFragment.TYPE_WIN));
        adapter.add(OrderFragment.newInstance(OrderFragment.TYPE_UNPAID));
        adapter.setTitles(new String[]{getString(R.string.order_all), getString(R.string.order_going), getString(R.string.order_win), getString(R.string.order_unpaid)});
        mBinding.viewPager.setAdapter(adapter);
        mBinding.viewPager.setOffscreenPageLimit(adapter.getCount());
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

        int selection = getIntent().getIntExtra(KEY_SELECTION, 0);
        mBinding.viewPager.setCurrentItem(selection, true);
    }
}
