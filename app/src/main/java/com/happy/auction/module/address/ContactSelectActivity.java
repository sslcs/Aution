package com.happy.auction.module.address;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.DecorationSpace;
import com.happy.auction.adapter.OnItemClickListener;
import com.happy.auction.base.BaseBackActivity;
import com.happy.auction.databinding.ActivitySelectAddressBinding;
import com.happy.auction.entity.item.Contact;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.ContactParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.ui.CustomDialog;
import com.happy.auction.utils.GsonSingleton;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 选择联系人界面<br/>
 *
 * @author LiuCongshan
 * @date 17-10-17
 */
public class ContactSelectActivity extends BaseBackActivity {
    public static final String EXTRA_CONTACT_ID = "contact_id";
    private final static int REQUEST_CODE = 100;
    protected ActivitySelectAddressBinding mBinding;
    private ContactSelectAdapter mAdapter;

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
        mAdapter = new ContactSelectAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener<Contact>() {
            @Override
            public void onItemClick(View view, Contact item, int position) {
                mAdapter.setSelectPosition(position);
            }
        });
        mBinding.vList.setAdapter(mAdapter);

        loadData();
    }

    protected void loadData() {
        ContactParam param = new ContactParam();
        BaseRequest<ContactParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                mAdapter.setLoaded();
                Type type = new TypeToken<DataResponse<ArrayList<Contact>>>() {}.getType();
                DataResponse<ArrayList<Contact>> obj = GsonSingleton.get().fromJson(response, type);
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
        new CustomDialog.Builder().content(getString(R.string.confirm_contact))
                .textLeft(getString(R.string.cancel))
                .textRight(getString(R.string.ok))
                .setOnClickRightListener(new CustomDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogFragment dialog) {
                        dialog.dismiss();
                        setResult();
                    }
                })
                .show(getSupportFragmentManager(), "confirm");
    }

    private void setResult() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CONTACT_ID, mAdapter.getItem(mAdapter.getSelectPosition()).vaid);
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
