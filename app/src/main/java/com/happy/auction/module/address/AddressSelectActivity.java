package com.happy.auction.module.address;

import android.content.Intent;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.adapter.OnItemClickListener;
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
 * Created by LiuCongshan on 17-10-19.<br/>
 * 选择收货地址界面
 */
public class AddressSelectActivity extends BaseAddressSelectActivity {
    public static final String EXTRA_ADDRESS = "address";
    public static final String EXTRA_ID = "aid";
    private AddressSelectAdapter mAdapter;

    public static Intent newIntent(int aid) {
        Intent intent = new Intent(AppInstance.getInstance(), AddressSelectActivity.class);
        intent.putExtra(EXTRA_ID, aid);
        return intent;
    }

    @Override
    protected void initChildLayout() {
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        mAdapter = new AddressSelectAdapter(id);
        mAdapter.setOnItemClickListener(new OnItemClickListener<Address>() {
            @Override
            public void onItemClick(View view, Address item, int position) {
                mAdapter.setSelectPosition(position);
            }
        });
        mBinding.vList.setAdapter(mAdapter);
    }

    @Override
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

    @Override
    protected Intent getManageIntent() {
        return AddressActivity.newIntent();
    }

    @Override
    protected void putIntentData(Intent intent) {
        intent.putExtra(EXTRA_ADDRESS, mAdapter.getItem(mAdapter.getSelectPosition()));
    }
}