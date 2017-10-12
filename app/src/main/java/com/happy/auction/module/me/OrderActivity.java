package com.happy.auction.module.me;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import com.happy.auction.R;
import com.happy.auction.adapter.ViewPagerAdapter;
import com.happy.auction.base.BaseActivity;
import com.happy.auction.databinding.ActivityTabPagerBinding;

public class OrderActivity extends BaseActivity {
    private static final String KEY_SELECTION = "selection";
    private ActivityTabPagerBinding binding;

    public static Intent newIntent(Context context, int selection) {
        Intent intent = new Intent(context, OrderActivity.class);
        intent.putExtra(KEY_SELECTION, selection);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tab_pager);
        initLayout();
    }

    private void initLayout() {
        binding.tvToolbarTitle.setText(R.string.order);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.add(OrderFragment.newInstance(OrderFragment.TYPE_ALL));
        adapter.add(OrderFragment.newInstance(OrderFragment.TYPE_GOING));
        adapter.add(OrderFragment.newInstance(OrderFragment.TYPE_WIN));
        adapter.add(OrderFragment.newInstance(OrderFragment.TYPE_UNPAID));
        adapter.setTitles(new String[]{getString(R.string.order_all), getString(R.string.order_going), getString(R.string.order_win), getString(R.string.order_unpaid)});
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(adapter.getCount());
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        int selection = getIntent().getIntExtra(KEY_SELECTION, 0);
        binding.viewPager.setCurrentItem(selection, true);
    }
}
