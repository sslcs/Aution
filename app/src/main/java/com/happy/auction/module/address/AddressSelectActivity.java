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
import com.happy.auction.adapter.OnItemClickListener;
import com.happy.auction.base.BaseBackActivity;
import com.happy.auction.databinding.ActivitySelectAddressBinding;
import com.happy.auction.entity.item.Address;
import com.happy.auction.entity.param.AddressParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 选择收货地址界面
 *
 * @author LiuCongshan
 * @date 17-10-19
 */
public class AddressSelectActivity extends BaseBackActivity {
    public static final String EXTRA_ADDRESS = "address";
    public static final String EXTRA_ID = "aid";
    private final static int REQUEST_CODE = 100;

    protected ActivitySelectAddressBinding mBinding;
    private AddressSelectAdapter mAdapter;

    public static Intent newIntent(int aid) {
        Intent intent = new Intent(AppInstance.getInstance(), AddressSelectActivity.class);
        intent.putExtra(EXTRA_ID, aid);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_address);
        initLayout();
    }

    private void initLayout() {
        mBinding.vList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.vList.addItemDecoration(new DecorationSpace(10));
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        mAdapter = new AddressSelectAdapter(id);
        mAdapter.setOnItemClickListener(new OnItemClickListener<Address>() {
            @Override
            public void onItemClick(View view, Address item, int position) {
                mAdapter.setSelectPosition(position);
            }
        });
        mBinding.vList.setAdapter(mAdapter);

        loadData();
    }

    protected void loadData() {
        AddressParam param = new AddressParam();
        BaseRequest<AddressParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                mAdapter.setLoaded();
                Type type = new TypeToken<DataResponse<ArrayList<Address>>>() {}.getType();
                DataResponse<ArrayList<Address>> obj = GsonSingleton.get().fromJson(response, type);
                if (obj.data != null && !obj.data.isEmpty()) {
                    mAdapter.clear();
                    mAdapter.addAll(obj.data);
                }
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                mAdapter.setLoaded();
            }
        });
    }

    public void onClickConfirm(View view) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ADDRESS, mAdapter.getItem(mAdapter.getSelectPosition()));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            loadData();
        }
    }

    public void onClickManage(View view) {
        startActivityForResult(AddressActivity.newIntent(), REQUEST_CODE);
    }
}
