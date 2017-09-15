package com.happy.auction.main.me;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.happy.auction.R;
import com.happy.auction.adapter.ViewPagerAdapter;
import com.happy.auction.base.BaseActivity;
import com.happy.auction.databinding.ActivityTabPagerBinding;

public class OrderActivity extends BaseActivity {
    private static final String KEY_SELECTION = "selection";
    private ActivityTabPagerBinding binding;

    public static Intent newInstance(Context context, int selection) {
        Intent intent = new Intent(context, OrderActivity.class);
        intent.putExtra(KEY_SELECTION, selection);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tab_pager);
        initLayout();
    }

    private void initLayout() {
        binding.tvToolbarTitle.setText(R.string.record);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.add(OrderFragment.newInstance(OrderFragment.TYPE_ALL));
        adapter.add(OrderFragment.newInstance(OrderFragment.TYPE_GOING));
        adapter.add(OrderFragment.newInstance(OrderFragment.TYPE_WIN));
        adapter.add(OrderFragment.newInstance(OrderFragment.TYPE_UNPAID));
        adapter.setTitles(new String[]{getString(R.string.record_all),
                getString(R.string.record_going),
                getString(R.string.record_win),
                getString(R.string.record_unpaid)});
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        int selection = getIntent().getIntExtra(KEY_SELECTION, 0);
        binding.viewPager.setCurrentItem(selection, true);
    }
}