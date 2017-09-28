package com.happy.auction.module.me;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.happy.auction.R;
import com.happy.auction.adapter.ViewPagerAdapter;
import com.happy.auction.base.BaseActivity;
import com.happy.auction.databinding.ActivityTabPagerBinding;

public class BalanceActivity extends BaseActivity {
    private static final String KEY_SELECTION = "selection";
    private ActivityTabPagerBinding binding;

    public static Intent newInstance(Context context, int selection) {
        Intent intent = new Intent(context, BalanceActivity.class);
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
        binding.tvToolbarTitle.setText(R.string.account_balance);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.add(CoinFragment.newInstance(CoinFragment.TYPE_COIN));
        adapter.add(CoinFragment.newInstance(CoinFragment.TYPE_FREE));
        adapter.setTitles(new String[]{getString(R.string.auction_coin), getString(R.string.free_coin)});
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        int selection = getIntent().getIntExtra(KEY_SELECTION, 0);
        binding.viewPager.setCurrentItem(selection, true);
    }
}
