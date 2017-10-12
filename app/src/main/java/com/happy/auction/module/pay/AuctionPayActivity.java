package com.happy.auction.module.pay;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.DecorationSpace;
import com.happy.auction.adapter.OnItemClickListener;
import com.happy.auction.databinding.ActivityAuctionPayBinding;
import com.happy.auction.entity.item.ItemGoods;
import com.happy.auction.entity.item.ItemPayType;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.BidParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.PayConfirmResponse;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.ToastUtil;

import java.lang.reflect.Type;

public class AuctionPayActivity extends BasePayActivity {
    private static final String KEY_EXTRA_DATA = "EXTRA_DATA";
    private static final String KEY_EXTRA_COUNT = "EXTRA_COUNT";

    private ActivityAuctionPayBinding mBinding;
    private ItemGoods mData;
    private int mCount;
    private PayTypeAdapter mAdapter;

    public static Intent newIntent(Context context, ItemGoods data, int count) {
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
        mData = (ItemGoods) getIntent().getSerializableExtra(KEY_EXTRA_DATA);
        mCount = getIntent().getIntExtra(KEY_EXTRA_COUNT, 0);
        mBinding.setData(mData);
        mBinding.setPay(new PayData(mCount));

        mBinding.vList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.vList.addItemDecoration(new DecorationSpace());
        mAdapter = new PayTypeAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mAdapter.setSelectedPosition(position);
            }
        });
        mBinding.vList.setAdapter(mAdapter);

        loadData(mAdapter);
    }

    public void onClickPay(View view) {
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

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                ToastUtil.show(message);
            }
        });
    }
}
