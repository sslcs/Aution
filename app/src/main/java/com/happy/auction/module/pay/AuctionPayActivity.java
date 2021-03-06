package com.happy.auction.module.pay;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.databinding.ActivityAuctionPayBinding;
import com.happy.auction.entity.item.BaseGoods;
import com.happy.auction.entity.item.ItemPayType;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.BidParam;
import com.happy.auction.entity.param.ConfigParam;
import com.happy.auction.entity.response.ConfigInfo;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.PayConfirmResponse;
import com.happy.auction.module.main.WebActivity;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.EventAgent;
import com.happy.auction.utils.GsonSingleton;

import java.lang.reflect.Type;

/**
 * 出价充值界面
 *
 * @author LiuCongshan
 * @date 17-10-23
 */
public class AuctionPayActivity extends BasePayActivity {
    private static final String KEY_EXTRA_DATA = "EXTRA_DATA";
    private static final String KEY_EXTRA_COUNT = "EXTRA_COUNT";

    private ActivityAuctionPayBinding mBinding;
    private BaseGoods mData;
    private ConfigInfo mConfigInfo;
    private int mCount;
    private PayData mPayData;

    public static Intent newIntent(Context context, BaseGoods data, int count) {
        Intent intent = new Intent(context, AuctionPayActivity.class);
        intent.putExtra(KEY_EXTRA_DATA, data);
        intent.putExtra(KEY_EXTRA_COUNT, count);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_auction_pay);

        initLayout();
    }

    private void initLayout() {
        mData = (BaseGoods) getIntent().getSerializableExtra(KEY_EXTRA_DATA);
        mCount = getIntent().getIntExtra(KEY_EXTRA_COUNT, 0);
        mPayData = new PayData(mCount);
        mBinding.setData(mData);
        mBinding.setPay(mPayData);
        if (AppInstance.getInstance().getBalance() > 0) {
            mBinding.tvMinus.setSelected(true);
        }

        initList(mBinding.vList);

        getConfig();
    }

    public void onClickPay(View view) {
        EventAgent.onEvent(R.string.goods_pay_confirm);
        final ItemPayType current = mAdapter.getItem(mAdapter.getSelectedPosition());
        BidParam param = new BidParam();
        param.pay_type = current.pay_type;
        param.sid = mData.sid;
        param.buy = mCount;
        param.take_coin = AppInstance.getInstance().getBalance();
        BaseRequest<BidParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<PayConfirmResponse>>() {}.getType();
                DataResponse<PayConfirmResponse> obj = GsonSingleton.get().fromJson(response, type);
                pay(obj.data, current);
            }
        });
    }

    private void getConfig() {
        ConfigParam param = new ConfigParam();
        BaseRequest<ConfigParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<ConfigInfo>>() {}.getType();
                DataResponse<ConfigInfo> obj = GsonSingleton.get().fromJson(response, type);
                if (obj.data != null) {
                    mConfigInfo = obj.data;
                    mBinding.tvInvite.setVisibility(View.VISIBLE);
                    mBinding.tvInvite.setText(mConfigInfo.title);
                }
            }
        });
    }

    public void onClickInvite(View view) {
        startActivity(WebActivity.newIntent(mConfigInfo.title, mConfigInfo.link));
    }

    public void onClickBalance(View view) {
        if (AppInstance.getInstance().getBalance() == 0) {
            return;
        }

        boolean select = !mBinding.tvMinus.isSelected();
        mBinding.tvMinus.setSelected(select);
        int minus = select ? AppInstance.getInstance().getBalance() : 0;
        mPayData.minus.set(minus);
    }
}
