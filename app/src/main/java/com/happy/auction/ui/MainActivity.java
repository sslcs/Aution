package com.happy.auction.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.happy.auction.R;
import com.happy.auction.adapter.ViewPagerAdapter;
import com.happy.auction.databinding.ActivityMainBinding;
import com.happy.auction.tab.category.TabCategoryFragment;
import com.happy.auction.tab.home.TabHomeFragment;
import com.happy.auction.tab.latest.TabLatestFragment;
import com.happy.auction.tab.me.TabMeFragment;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initLayout();
    }

    private void initLayout() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.add(TabHomeFragment.newInstance());
        adapter.add(TabLatestFragment.newInstance());
        adapter.add(TabCategoryFragment.newInstance());
        adapter.add(TabMeFragment.newInstance());
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        TabLayout.Tab tab = binding.tabLayout.getTabAt(0);
        if (tab != null) tab.setCustomView(R.layout.tab_home);
        tab = binding.tabLayout.getTabAt(1);
        if (tab != null) tab.setCustomView(R.layout.tab_latest);
        tab = binding.tabLayout.getTabAt(2);
        if (tab != null) tab.setCustomView(R.layout.tab_category);
        tab = binding.tabLayout.getTabAt(3);
        if (tab != null) tab.setCustomView(R.layout.tab_me);
    }

    public void onClickJoin(View view) {
        startActivity(AuctionDetailActivity.newIntent());
    }
}
