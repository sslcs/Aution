package com.happy.auction.module.pay;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.R;
import com.happy.auction.adapter.DecorationSpace;
import com.happy.auction.adapter.OnItemClickListener;
import com.happy.auction.base.BaseActivity;
import com.happy.auction.databinding.ActivityOrderPayBinding;
import com.happy.auction.entity.item.ItemOrder;
import com.happy.auction.entity.item.ItemPayType;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.PayConfirmParam;
import com.happy.auction.entity.param.PayOptionsParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.PayConfirmResponse;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.PackageUtils;
import com.happy.auction.utils.ToastUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class OrderPayActivity extends BaseActivity {
    private static final String KEY_EXTRA = "EXTRA";

    private ActivityOrderPayBinding mBinding;
    private ItemOrder mData;
    private PayTypeAdapter mAdapter;

    public static Intent newIntent(Context context, ItemOrder data) {
        Intent intent = new Intent(context, OrderPayActivity.class);
        intent.putExtra(KEY_EXTRA, data);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_pay);

        initLayout();
    }

    private void initLayout() {
        mData = (ItemOrder) getIntent().getSerializableExtra(KEY_EXTRA);
        mBinding.setData(mData);

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

        loadData();
    }

    private void loadData() {
        PayOptionsParam param = new PayOptionsParam();
        BaseRequest<PayOptionsParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<ArrayList<ItemPayType>>>() {}.getType();
                DataResponse<ArrayList<ItemPayType>> obj = GsonSingleton.get().fromJson(response, type);
                mAdapter.addAll(obj.data);
            }
        });
    }

    public void onClickPay(View view) {
        ItemPayType current = mAdapter.getItem(mAdapter.getSelectedPosition());
        PayConfirmParam param = new PayConfirmParam();
        param.pay_type = current.pay_type;
        param.sid = mData.sid;
        BaseRequest<PayConfirmParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<PayConfirmResponse>>() {}.getType();
                DataResponse<PayConfirmResponse> obj = GsonSingleton.get().fromJson(response, type);
                pay(obj.data);
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                ToastUtil.show(message);
            }
        });
    }

    private void pay(PayConfirmResponse response) {
        if (ItemPayType.WEB_ALIPAY == response.pay_type) {
            if (!PackageUtils.isInstalledAlipay(this)) {
                ToastUtil.show(R.string.error_alipay);
                return;
            }
            Uri uri = Uri.parse(response.params);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (ItemPayType.TH_PAY == response.pay_type) {
            if (!PackageUtils.isInstalledAlipay(this)) {
                ToastUtil.show(R.string.error_alipay);
                return;
            }
            ItemPayType current = mAdapter.getItem(mAdapter.getSelectedPosition());
            ThPay.getInstance(getSupportFragmentManager(), this).pay(response.params, current.app_id);
        } else if (ItemPayType.SDK_ALIPAY == response.pay_type) {
            if (!PackageUtils.isInstalledAlipay(this)) {
                ToastUtil.show(R.string.error_alipay);
                return;
            }
            WftPay.getInstance(this).pay(response.params);
        }
    }
}
