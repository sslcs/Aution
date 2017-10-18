package com.happy.auction.module.address;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.base.BaseActivity;
import com.happy.auction.databinding.ActivityAddressEditBinding;
import com.happy.auction.entity.item.Address;
import com.happy.auction.entity.param.AddressEditParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.TownParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.TownResponse;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.ToastUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by LiuCongshan on 17-10-16.
 * 收货地址编辑界面
 */

public class AddressEditActivity extends BaseActivity {
    private static final String KEY_DATA = "DATA";
    private ActivityAddressEditBinding mBinding;
    private String mProvince, mCity, mDistrict, mTown, mAid;
    private ArrayList<AddressRecord> mTownList;
    private Address mData;

    public static Intent newIntent() {
        return new Intent(AppInstance.getInstance(), AddressEditActivity.class);
    }

    public static Intent newIntent(Address data) {
        Intent intent = newIntent();
        intent.putExtra(KEY_DATA, data);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_address_edit);

        initLayout();
    }

    private void initLayout() {
        if (getIntent().hasExtra(KEY_DATA)) {
            mData = (Address) getIntent().getSerializableExtra(KEY_DATA);
            mBinding.setData(mData);
        }
    }

    public void onClickStore(View view) {
        AddressEditParam param = new AddressEditParam(mData != null);
        param.username = mBinding.etUsername.getText().toString().trim();
        param.phone = mBinding.etPhone.getText().toString().trim();
        param.province = mProvince;
        param.city = mCity;
        param.district = mDistrict;
        param.town = mTown;
        param.street = mBinding.etStreet.getText().toString();
        if (mData != null) {
            param.aid = mData.aid;
        }
        BaseRequest<AddressEditParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                ToastUtil.show(message);
            }
        });
    }

    public void onClickArea(View view) {
        AddressSelector selector = AddressSelector.newInstance(new AddressSelector.OnSelectListener() {
            @Override
            public void onSelect(String province, String city, String district, String aid) {
                mProvince = province;
                mCity = city;
                mDistrict = district;
                mAid = aid;
                mBinding.tvArea.setText(mProvince + mCity + mDistrict);

                loadTown();
            }
        });
        selector.show(getSupportFragmentManager(), "address");
    }

    public void onClickTown(View view) {
        TownSelector selector = TownSelector.newInstance(mTownList, new TownSelector.OnSelectListener() {
            @Override
            public void onSelect(String town) {
                mTown = town;
                mBinding.tvTown.setText(mTown);
            }
        });
        selector.show(getSupportFragmentManager(), "town");
    }

    private void loadTown() {
        mTown = "";
        mBinding.tvTown.setText("");
        mBinding.tvTown.setVisibility(View.GONE);
        mBinding.tvTownLabel.setVisibility(View.GONE);
        TownParam param = new TownParam();
        param.aid = mAid;
        BaseRequest<TownParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<TownResponse>>() {}.getType();
                DataResponse<TownResponse> obj = GsonSingleton.get().fromJson(response, type);
                if (obj.data.hasChild()) {
                    mBinding.tvTown.setVisibility(View.VISIBLE);
                    mBinding.tvTownLabel.setVisibility(View.VISIBLE);
                    mTownList = obj.data.childs;
                }
            }
        });
    }
}
