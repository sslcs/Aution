package com.happy.auction.module.address;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.DecorationSpace;
import com.happy.auction.adapter.OnViewClickListener;
import com.happy.auction.base.BaseBackActivity;
import com.happy.auction.databinding.ActivityAddressBinding;
import com.happy.auction.entity.item.Address;
import com.happy.auction.entity.param.AddressDefaultParam;
import com.happy.auction.entity.param.AddressDelParam;
import com.happy.auction.entity.param.AddressParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by LiuCongshan on 17-10-16.
 * 收货地址界面
 */
public class AddressActivity extends BaseBackActivity implements OnViewClickListener<Address> {
    private final static int REQUEST_ADD = 100;
    private final static int REQUEST_EDIT = 101;

    private ActivityAddressBinding mBinding;
    private AddressAdapter adapter;
    private boolean mDataChanged = false;

    public static Intent newIntent() {
        return new Intent(AppInstance.getInstance(), AddressActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_address);
        initLayout();
    }

    private void initLayout() {
        mBinding.vList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.vList.addItemDecoration(new DecorationSpace(10));
        adapter = new AddressAdapter();
        adapter.setOnViewClickListener(this);
        mBinding.vList.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        AddressParam param = new AddressParam();
        BaseRequest<AddressParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                adapter.setLoaded();
                Type type = new TypeToken<DataResponse<ArrayList<Address>>>() {}.getType();
                DataResponse<ArrayList<Address>> obj = GsonSingleton.get().fromJson(response, type);
                if (obj.data != null && !obj.data.isEmpty()) {
                    adapter.clear();
                    adapter.addAll(obj.data);
                }
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                adapter.setLoaded();
            }
        });
    }

    public void onClickAdd(View view) {
        startActivityForResult(AddressEditActivity.newIntent(), REQUEST_ADD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            mDataChanged = true;
            loadData();
        }
    }

    @Override
    public void onViewClick(View view, Address item, int position) {
        if (R.id.tv_default == view.getId()) {
            setDefault(item, position);
        } else if (R.id.tv_edit == view.getId()) {
            edit(item);
        } else if (R.id.tv_delete == view.getId()) {
            delete(item);
        }
    }

    private void setDefault(final Address item, final int position) {
        AddressDefaultParam param = new AddressDefaultParam();
        param.aid = item.aid;
        BaseRequest<AddressDefaultParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                adapter.setPositionDefault(position);
            }
        });
    }

    private void edit(Address item) {
        startActivityForResult(AddressEditActivity.newIntent(item), REQUEST_EDIT);
    }

    private void delete(final Address item) {
        AddressDelParam param = new AddressDelParam();
        param.aid = item.aid;
        BaseRequest<AddressDelParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                mDataChanged = true;
                int position = adapter.getPosition(item);
                adapter.removeItem(position);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDataChanged) {
            setResult(RESULT_OK);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
