package com.happy.auction.module.detail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.base.BaseBackActivity;
import com.happy.auction.databinding.ActivityChartBinding;
import com.happy.auction.entity.event.AuctionEndEvent;
import com.happy.auction.entity.event.BidEvent;
import com.happy.auction.entity.item.ItemGoods;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.TrendParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.TrendResponse;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.RxBus;
import com.happy.auction.utils.StringUtil;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Locale;

import io.reactivex.functions.Consumer;

/**
 * 成交价走势图界面
 *
 * @author LiuCongshan
 * @date 17-11-23
 */

public class ChartActivity extends BaseBackActivity {
    private static final String KEY_DATA = "data";
    private ActivityChartBinding mBinding;
    private ItemGoods mData;
    private TrendAdapter mAdapter;

    public static Intent newIntent(ItemGoods data) {
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
        mData = (ItemGoods) getIntent().getSerializableExtra(KEY_DATA);
        mBinding.setData(mData);
        mBinding.setActivity(this);
        listenEvents();

        mAdapter = new TrendAdapter();
        mAdapter.setMax(mData.market_price);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mBinding.vList.setLayoutManager(llm);

        loadData();
    }

    private void listenEvents() {
        RxBus.getDefault().subscribe(this, BidEvent.class, new Consumer<BidEvent>() {
            @Override
            public void accept(BidEvent event) throws Exception {
                if (event.sid == mData.sid && event.current_price > mData.current_price) {
                    mData.setCurrentPrice(event.current_price);
                }
            }
        });

        RxBus.getDefault().subscribe(this, AuctionEndEvent.class, new Consumer<AuctionEndEvent>() {
            @Override
            public void accept(AuctionEndEvent event) throws Exception {
                if (event.sid == mData.sid) {
                    mData.setStatus(0);
                }
            }
        });
    }

    private void loadData() {
        TrendParam param = new TrendParam();
        param.gid = mData.gid;
        BaseRequest<TrendParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<TrendResponse>>() {}.getType();
                DataResponse<TrendResponse> obj = GsonSingleton.get().fromJson(response, type);
                if (obj.data == null || obj.data.trend_data == null) {
                    return;
                }
                Collections.reverse(obj.data.trend_data);
                mAdapter.addAll(obj.data.trend_data);
                mBinding.vList.setAdapter(mAdapter);
                mBinding.vList.smoothScrollToPosition(obj.data.trend_data.size() - 1);
                String min = StringUtil.formatMoney(obj.data.min_price);
                mBinding.tvMin.setText(getString(R.string.chart_min, min));
                String max = StringUtil.formatMoney(obj.data.max_price);
                mBinding.tvMax.setText(getString(R.string.chart_max, max));
            }
        });
    }

    public String getPrice(int percent) {
        float price = mData.market_price * (percent / 100f / 100);
        return String.format(Locale.CHINA, "%.1f", price);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unsubscribe(this);
    }
}
