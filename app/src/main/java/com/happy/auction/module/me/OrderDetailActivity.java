package com.happy.auction.module.me;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.base.BaseActivity;
import com.happy.auction.databinding.ActivityOrderDetailBinding;
import com.happy.auction.entity.item.ItemOrder;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.OrderDetailParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.OrderDetail;
import com.happy.auction.module.pay.OrderPayActivity;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.StringUtil;

import java.lang.reflect.Type;

public class OrderDetailActivity extends BaseActivity {
    private static final String KEY_ITEM = "KEY_ITEM";
    private static final int REQUEST_CODE_PAY = 100;

    private ActivityOrderDetailBinding mBinding;
    private OrderDetail mData;
    private ItemOrder mOrder;

    public static Intent newIntent(ItemOrder item) {
        Intent intent = new Intent(AppInstance.getInstance(), OrderDetailActivity.class);
        intent.putExtra(KEY_ITEM, item);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        }
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);
        initLayout();
    }

    private void initLayout() {
        mOrder = (ItemOrder) getIntent().getSerializableExtra(KEY_ITEM);
        mData = new OrderDetail(mOrder);
        mBinding.setData(mData);
        setProgress();

        loadData();
    }

    private void setProgress() {
        mBinding.progress0.setSelected(mData.status >= 2);
        mBinding.progress1.setSelected(mData.status >= 3);
        mBinding.progress2.setSelected(mData.status >= 4);
        mBinding.progress3.setSelected(mData.status >= 5);
        mBinding.tvProgress0.setSelected(mData.status == 2);
        mBinding.tvProgress1.setSelected(mData.status == 3);
        mBinding.tvProgress2.setSelected(mData.status == 4);
        mBinding.tvProgress3.setSelected(mData.status >= 5);
        mBinding.tvTimeProgress0.setText(StringUtil.formatTimeMinute(mData.prize_time));
        if (mData.pay_time != 0)
            mBinding.tvTimeProgress1.setText(StringUtil.formatTimeMinute(mData.pay_time));
        if (mData.confirm_prize_time != 0)
            mBinding.tvTimeProgress2.setText(StringUtil.formatTimeMinute(mData.confirm_prize_time));
        if (mData.bask_time != 0)
            mBinding.tvTimeProgress3.setText(StringUtil.formatTimeMinute(mData.bask_time));
    }

    private void loadData() {
        OrderDetailParam param = new OrderDetailParam();
        param.pid = mData.pid;
        BaseRequest<OrderDetailParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<OrderDetail>>() {}.getType();
                DataResponse<OrderDetail> obj = GsonSingleton.get().fromJson(response, type);
                mData = obj.data;
                mBinding.setData(obj.data);
                setProgress();
                setBottom();
            }
        });
    }

    private void setBottom() {
        if (mData.status == 4) {
            // 已确认
            mBinding.btnBottom.setText(R.string.go_bask);
        } else if (mData.status >= 5) {
            // 已晒单
            mBinding.btnBottom.setText(R.string.check_bask);
        } else if (mData.status == 2) {
            // 已拍中
            mBinding.btnBottom.setText(R.string.go_pay);
        } else if (mData.type == 1) {
            // 已付款-实物
            mBinding.btnBottom.setText(R.string.confirm_address);
        } else {
            // 已付款-虚拟物品
            mBinding.btnBottom.setText(R.string.select_virtual_address);
        }
    }

    public void onClickBtnBottom(View view) {
        if (mData.status == 4) {
            // 已确认
        } else if (mData.status >= 5) {
            // 已晒单
        } else if (mData.status == 2) {
            // 已拍中
            Intent intent = OrderPayActivity.newIntent(this, mOrder);
            startActivityForResult(intent, REQUEST_CODE_PAY);
        } else if (mData.type == 1) {
            // 已付款-实物
        } else {
            // 已付款-虚拟物品
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}