package com.happy.auction.module.pay;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.R;
import com.happy.auction.adapter.DecorationSpace;
import com.happy.auction.adapter.OnItemClickListener;
import com.happy.auction.base.BaseBackActivity;
import com.happy.auction.entity.item.ItemPayType;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.PayOptionsParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.PayConfirmResponse;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.ui.CustomDialog;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.PackageUtils;
import com.happy.auction.utils.ToastUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 选择支付方式的基类
 *
 * @author LiuCongshan
 */
public abstract class BasePayActivity extends BaseBackActivity {
    protected PayTypeAdapter mAdapter;
    private boolean isPaying = false;

    protected void initList(RecyclerView vList) {
        vList.setLayoutManager(new LinearLayoutManager(this));
        vList.addItemDecoration(new DecorationSpace());
        mAdapter = new PayTypeAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener<ItemPayType>() {
            @Override
            public void onItemClick(View view, ItemPayType item, int position) {
                mAdapter.setSelectedPosition(position);
            }
        });
        vList.setAdapter(mAdapter);

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

    protected void pay(PayConfirmResponse response, ItemPayType current) {
        if (ItemPayType.WEB_ALIPAY == response.pay_type) {
            if (!PackageUtils.isInstalledAlipay(this)) {
                ToastUtil.show(R.string.error_alipay);
                return;
            }
            if (TextUtils.isEmpty(response.params)) {
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
            ThPay.getInstance(getSupportFragmentManager(), this).pay(response.params, current.app_id);
        } else if (ItemPayType.SDK_ALIPAY == response.pay_type) {
            if (!PackageUtils.isInstalledAlipay(this)) {
                ToastUtil.show(R.string.error_alipay);
                return;
            }
            WftPay.getInstance(this).pay(response.params);
        }

        isPaying = true;
        // FixMe
        onResume();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isPaying) {
            return;
        }
        isPaying = false;
        new CustomDialog.Builder().content(getString(R.string.tip_finish_pay))
                .textRight(getString(R.string.unpaid))
                .textLeft(getString(R.string.paid))
                .setOnClickLeftListener(new CustomDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogFragment dialog) {
                        dialog.dismiss();
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .show(getSupportFragmentManager(), "pay");
    }
}
