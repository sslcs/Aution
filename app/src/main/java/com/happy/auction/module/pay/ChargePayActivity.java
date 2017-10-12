package com.happy.auction.module.pay;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.R;
import com.happy.auction.adapter.DecorationSpace;
import com.happy.auction.adapter.OnItemClickListener;
import com.happy.auction.databinding.ActivityChargePayBinding;
import com.happy.auction.entity.item.ItemPayType;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.PayChargeParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.PayConfirmResponse;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.ToastUtil;

import java.lang.reflect.Type;

public class ChargePayActivity extends BasePayActivity {
    private final ObservableInt mAmount = new ObservableInt(10);
    private ActivityChargePayBinding mBinding;
    private PayTypeAdapter mAdapter;
    private View vCurrentAmount;

    public static Intent newIntent(Context context) {
        return new Intent(context, ChargePayActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_charge_pay);

        initLayout();
    }

    private void initLayout() {
        mBinding.setAmount(mAmount);
        mBinding.setActivity(this);

        vCurrentAmount = mBinding.tvAmount0;
        vCurrentAmount.requestFocus();
        vCurrentAmount.setSelected(true);

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
        PayChargeParam param = new PayChargeParam();
        param.pay_type = current.pay_type;
        param.coin = mAmount.get();
        BaseRequest<PayChargeParam> request = new BaseRequest<>(param);
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

    public void afterTextChanged(Editable editable) {
        try {
            int number = Integer.parseInt(editable.toString());
            mAmount.set(number);
        } catch (NumberFormatException e) {
            mAmount.set(0);
        }
    }

    public void onClickAmount(View view) {
        mBinding.etAmount.clearFocus();
        int amount = Integer.valueOf((String) view.getTag());
        if (vCurrentAmount == view && mAmount.get() == amount) return;
        mAmount.set(amount);
        vCurrentAmount.setSelected(false);
        vCurrentAmount = view;
        vCurrentAmount.setSelected(true);
    }
}