package com.happy.auction.module.pay;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.base.BaseBackActivity;
import com.happy.auction.databinding.ActivityChartBinding;
import com.happy.auction.entity.item.BaseGoods;
import com.happy.auction.entity.item.ItemTrend;
import com.happy.auction.module.detail.TrendAdapter;

import java.util.Random;

/**
 * 成交价走势图界面
 *
 * @author LiuCongshan
 * @date 17-11-23
 */

public class ChartActivity extends BaseBackActivity {
    private static final String KEY_DATA = "data";
    private ActivityChartBinding mBinding;
    private BaseGoods mData;
    private TrendAdapter mAdapter;

    public static Intent newIntent(BaseGoods data) {
        Intent intent = new Intent(AppInstance.getInstance(), ChartActivity.class);
        intent.putExtra(KEY_DATA, data);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chart);
        initLayout();
    }

    private void initLayout() {
        mData = (BaseGoods) getIntent().getSerializableExtra(KEY_DATA);
        mBinding.setData(mData);

        mAdapter = new TrendAdapter();
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        llm.setReverseLayout(true);
        mBinding.vList.setLayoutManager(llm);

        loadData();
    }

    private void loadData() {
        for (int i = 0; i < 10; i++) {
            ItemTrend item = new ItemTrend();
            item.deal_price = new Random().nextInt(100);
            item.period = String.valueOf(1234560 + i);
            mAdapter.addItem(item);
        }
        mAdapter.setMax(110);
        mBinding.vList.setAdapter(mAdapter);
    }
}
