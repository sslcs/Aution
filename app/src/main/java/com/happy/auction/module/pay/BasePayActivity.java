package com.happy.auction.module.pay;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.R;
import com.happy.auction.adapter.BaseAdapter;
import com.happy.auction.base.BaseActivity;
import com.happy.auction.entity.item.ItemPayType;
import com.happy.auction.entity.param.BaseRequest;
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

public abstract class BasePayActivity extends BaseActivity {
    protected <B extends ViewDataBinding> void loadData(final BaseAdapter<ItemPayType, B> adapter) {
        PayOptionsParam param = new PayOptionsParam();
        BaseRequest<PayOptionsParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<ArrayList<ItemPayType>>>() {}.getType();
                DataResponse<ArrayList<ItemPayType>> obj = GsonSingleton.get().fromJson(response, type);
                adapter.addAll(obj.data);
            }
        });
    }

    protected void pay(PayConfirmResponse response, ItemPayType current) {
        if (ItemPayType.WEB_ALIPAY == response.pay_type) {
            if (!PackageUtils.isInstalledAlipay(this)) {
                ToastUtil.show(R.string.error_alipay);
                return;
            }
            if (TextUtils.isEmpty(response.params)) return;
            Uri uri = Uri.parse(response.params);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (ItemPayType.TH_PAY == response.pay_type) {
            if (!PackageUtils.isInstalledAlipay(this)) {
                ToastUtil.show(R.string.error_alipay);
                return;
            }
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
