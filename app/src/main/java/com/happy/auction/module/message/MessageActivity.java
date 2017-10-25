package com.happy.auction.module.message;

import android.content.Intent;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.ViewPagerAdapter;
import com.happy.auction.base.BaseTabActivity;

/**
 * 消息页面
 *
 * @author LiuCongshan
 */
public class MessageActivity extends BaseTabActivity {
    public static Intent newIntent() {
        return new Intent(AppInstance.getInstance(), MessageActivity.class);
    }

    @Override
    protected void initLayout() {
        mBinding.tvToolbarTitle.setText(R.string.title_message);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.add(MessageFragment.newInstance(MessageFragment.TYPE_AUCTION));
        adapter.add(MessageFragment.newInstance(MessageFragment.TYPE_DELIVERY));
        adapter.add(MessageFragment.newInstance(MessageFragment.TYPE_NOTICE));
        adapter.setTitles(new String[]{getString(R.string.message_auction), getString(R.string.message_delivery), getString(R.string.message_notice)});
        mBinding.viewPager.setAdapter(adapter);
        mBinding.viewPager.setOffscreenPageLimit(adapter.getCount());
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

        int selection = getIntent().getIntExtra(KEY_SELECTION, 0);
        mBinding.viewPager.setCurrentItem(selection, true);
    }
}
