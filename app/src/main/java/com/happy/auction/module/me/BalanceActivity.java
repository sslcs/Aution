package com.happy.auction.module.me;

import android.content.Intent;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.ViewPagerAdapter;
import com.happy.auction.base.BaseTabActivity;

/**
 * 余额页面
 *
 * @author cs
 */
public class BalanceActivity extends BaseTabActivity {
    /**
     * @param selection 选中标签位置
     * @return Intent for this Activity
     */
    public static Intent newIntent(int selection) {
        Intent intent = new Intent(AppInstance.getInstance(), BalanceActivity.class);
        intent.putExtra(KEY_SELECTION, selection);
        return intent;
    }

    @Override
    protected void initLayout() {
        mBinding.tvToolbarTitle.setText(R.string.account_balance);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.add(CoinFragment.newInstance(CoinFragment.TYPE_COIN));
        adapter.add(CoinFragment.newInstance(CoinFragment.TYPE_FREE));
        adapter.setTitles(new String[]{getString(R.string.auction_coin), getString(R.string.free_coin)});
        mBinding.viewPager.setAdapter(adapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

        int selection = getIntent().getIntExtra(KEY_SELECTION, 0);
        mBinding.viewPager.setCurrentItem(selection, true);
    }
}
