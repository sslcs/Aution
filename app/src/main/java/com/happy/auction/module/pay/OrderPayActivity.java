package com.happy.auction.module.pay;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.databinding.ActivityOrderPayBinding;
import com.happy.auction.entity.item.ItemOrder;
import com.happy.auction.entity.item.ItemPayType;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.PayConfirmParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.PayConfirmResponse;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.EventAgent;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.ToastUtil;

import java.lang.reflect.Type;


/**
 * 订单支付界面
 *
 * @author LiuCongshan
 * @date 17-10-23
 */
public class OrderPayActivity extends BasePayActivity {
    private static final String KEY_EXTRA = "EXTRA";

    private ActivityOrderPayBinding mBinding;
    private ItemOrder mData;

    public static Intent newIntent(ItemOrder data) {
        Intent intent = new Intent(AppInstance.getInstance(), OrderPayActivity.class);
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

        initList(mBinding.vList);
    }

    public void onClickPay(View view) {
        EventAgent.onEvent(R.string.reward_pay_confirm);
        final ItemPayType current = mAdapter.getItem(mAdapter.getSelectedPosition());
        PayConfirmParam param = new PayConfirmParam();
        param.pay_type = current.pay_type;
        param.sid = mData.sid;
        BaseRequest<PayConfirmParam> request = new BaseRequest<>(param);
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
