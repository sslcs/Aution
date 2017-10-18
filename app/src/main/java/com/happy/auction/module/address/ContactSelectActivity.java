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
import com.happy.auction.base.BaseActivity;
import com.happy.auction.databinding.ActivitySelectAddressBinding;
import com.happy.auction.entity.item.Contact;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.ContactParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by LiuCongshan on 17-10-17.<br/>
 * 选择联系人界面
 */
public class ContactSelectActivity extends BaseActivity {
    public static final String KEY_CONTACT_ID = "CONTACT";
    private final static int REQUEST_CODE = 100;
    private ActivitySelectAddressBinding mBinding;
    private ContactSelectAdapter adapter;

    public static Intent newIntent() {
        return new Intent(AppInstance.getInstance(), ContactSelectActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_address);
        initLayout();
    }

    private void initLayout() {
        mBinding.tvToolbarTitle.setText(R.string.select_contact);

        mBinding.vList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.vList.addItemDecoration(new DecorationSpace(10));
        adapter = new ContactSelectAdapter();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                adapter.setSelectPosition(position);
            }
        });
        mBinding.vList.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        ContactParam param = new ContactParam();
        BaseRequest<ContactParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                adapter.setLoaded();
                Type type = new TypeToken<DataResponse<ArrayList<Contact>>>() {}.getType();
                DataResponse<ArrayList<Contact>> obj = GsonSingleton.get().fromJson(response, type);
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

    public void onClickConfirm(View view) {
        Intent intent = new Intent();
        intent.putExtra(KEY_CONTACT_ID, adapter.getItem(adapter.getSelectPosition()).vaid);
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
        startActivityForResult(ContactActivity.newIntent(), REQUEST_CODE);
    }
}
