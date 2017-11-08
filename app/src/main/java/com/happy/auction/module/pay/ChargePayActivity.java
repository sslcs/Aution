package com.happy.auction.module.pay;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.databinding.ActivityChargePayBinding;
import com.happy.auction.entity.item.ItemPayType;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.PayChargeParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.PayConfirmResponse;
import com.happy.auction.module.me.BalanceActivity;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.EventAgent;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.ToastUtil;

import java.lang.reflect.Type;

/**
 * 充值支付界面
 *
 * @author LiuCongshan
 * @date 17-10-23
 */
public class ChargePayActivity extends BasePayActivity {
    private final ObservableInt mAmount = new ObservableInt(10);
    private ActivityChargePayBinding mBinding;
    private View vCurrentAmount;

    public static Intent newIntent() {
        return new Intent(AppInstance.getInstance(), ChargePayActivity.class);
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

        mBinding.etAmount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.performClick();
                return false;
            }
        });

        vCurrentAmount = mBinding.tvAmount0;
        vCurrentAmount.requestFocus();
        vCurrentAmount.setSelected(true);

        initList(mBinding.vList);
    }

    public void onClickPay(View view) {
        EventAgent.onEvent(R.string.recharge_pay);
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
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mBinding.etAmount.getWindowToken(), 0);
        }

        int amount = Integer.valueOf((String) view.getTag());
        if (vCurrentAmount == view && mAmount.get() == amount) {
            return;
        }
        mAmount.set(amount);
        vCurrentAmount.setSelected(false);
        vCurrentAmount = view;
        vCurrentAmount.setSelected(true);
    }

    public void onclickEdit(View view) {
        try {
            int number = Integer.parseInt(mBinding.etAmount.getText().toString());
            mAmount.set(number);
        } catch (NumberFormatException e) {
            mAmount.set(0);
        }
        vCurrentAmount.setSelected(false);
        vCurrentAmount = view;
        vCurrentAmount.setSelected(true);
    }

    @Override
    protected void paySuccess() {
        Intent intent = BalanceActivity.newIntent(0);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
