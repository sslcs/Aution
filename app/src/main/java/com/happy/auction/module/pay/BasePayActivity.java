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
import com.happy.auction.entity.param.ChargeStatusParam;
import com.happy.auction.entity.param.PayOptionsParam;
import com.happy.auction.entity.response.ChargeStatusResponse;
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
    private String mTradeNumber;

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
        mTradeNumber = response.tradenum;
        if (ItemPayType.WEB_ALIPAY == response.pay_type) {
            if (!PackageUtils.isInstalledAlipay(this)) {
                ToastUtil.show(R.string.error_alipay);
                return;
            }
            if (TextUtils.isEmpty(response.params)) {
                return;
            }
            openBrowser(response.params);
        } else if (ItemPayType.SDK_ALIPAY == response.pay_type) {
            if (!PackageUtils.isInstalledAlipay(this)) {
                ToastUtil.show(R.string.error_alipay);
                return;
            }
            openBrowser(response.params);
        } else if (ItemPayType.SDK_QQ == response.pay_type) {
            if (!PackageUtils.isInstalledQQ(this)) {
                ToastUtil.show(R.string.error_qq);
                return;
            }
            openBrowser(response.params);
        } else {
            openBrowser(response.params);
        }

        isPaying = true;
    }

    private void openBrowser(String url) {
        Uri uri = Uri.parse(url);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isPaying) {
            return;
        }

        isPaying = false;
        queryStatus();
    }

    private void queryStatus() {
        ChargeStatusParam param = new ChargeStatusParam();
        param.exorderno = mTradeNumber;
        BaseRequest<ChargeStatusParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<ChargeStatusResponse>>() {}.getType();
                DataResponse<ChargeStatusResponse> obj = GsonSingleton.get().fromJson(response, type);
                if (obj.data != null && obj.data.status == 3) {
                    paySuccess();
                } else {
                    showTip();
                }
            }
        });
    }

    protected void paySuccess() {
        setResult(RESULT_OK);
        finish();
    }

    protected void showTip() {
        new CustomDialog.Builder().content(getString(R.string.tip_finish_pay))
                .textRight(getString(R.string.unpaid))
                .textLeft(getString(R.string.paid))
                .setOnClickLeftListener(new CustomDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogFragment dialog) {
                        dialog.dismiss();
                        paySuccess();
                    }
                })
                .show(getSupportFragmentManager(), "pay");
    }
}
