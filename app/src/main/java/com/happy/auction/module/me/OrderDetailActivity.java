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
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.StringUtil;

import java.lang.reflect.Type;

public class OrderDetailActivity extends BaseActivity {
    private static final String KEY_ITEM = "KEY_ITEM";
    private ActivityOrderDetailBinding binding;
    private OrderDetail mData;

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);
        initLayout();
    }

    private void initLayout() {
        ItemOrder item = (ItemOrder) getIntent().getSerializableExtra(KEY_ITEM);
        mData = new OrderDetail(item);
        binding.setData(mData);
        setProgress();

        loadData();
    }

    private void setProgress() {
        binding.progress0.setSelected(mData.status >= 2);
        binding.progress1.setSelected(mData.status >= 3);
        binding.progress2.setSelected(mData.status >= 4);
        binding.progress3.setSelected(mData.status >= 5);
        binding.tvProgress0.setSelected(mData.status == 2);
        binding.tvProgress1.setSelected(mData.status == 3);
        binding.tvProgress2.setSelected(mData.status == 4);
        binding.tvProgress3.setSelected(mData.status >= 5);
        binding.tvTimeProgress0.setText(StringUtil.formatTimeMinute(mData.prize_time));
        if (mData.pay_time != 0)
            binding.tvTimeProgress1.setText(StringUtil.formatTimeMinute(mData.pay_time));
        if (mData.confirm_prize_time != 0)
            binding.tvTimeProgress2.setText(StringUtil.formatTimeMinute(mData.confirm_prize_time));
        if (mData.bask_time != 0)
            binding.tvTimeProgress3.setText(StringUtil.formatTimeMinute(mData.bask_time));
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
                binding.setData(obj.data);
                setProgress();
                setBottom();
            }
        });
    }

    private void setBottom() {
        if (mData.status == 4) {
            // 已确认
            binding.btnBottom.setText(R.string.go_bask);
        } else if (mData.status >= 5) {
            // 已晒单
            binding.btnBottom.setText(R.string.check_bask);
        } else if (mData.status == 2) {
            // 已拍中
            binding.btnBottom.setText(R.string.go_pay);
        } else if (mData.type == 1) {
            // 已付款-实物
            binding.btnBottom.setText(R.string.confirm_address);
        } else {
            // 已付款-虚拟物品
            binding.btnBottom.setText(R.string.select_virtual_address);
        }
    }

    public void onClickBtnBottom(View view) {
        if (mData.status == 4) {
            // 已确认
        } else if (mData.status >= 5) {
            // 已晒单
        } else if (mData.status == 2) {
            // 已拍中
        } else if (mData.type == 1) {
            // 已付款-实物
        } else {
            // 已付款-虚拟物品
        }
    }
}
