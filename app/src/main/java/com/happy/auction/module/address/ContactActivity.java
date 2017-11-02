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
import com.happy.auction.entity.item.Contact;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.ContactDelParam;
import com.happy.auction.entity.param.ContactParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 联系人界面
 *
 * @author LiuCongshan
 * @date 17-10-17
 */
public class ContactActivity extends BaseBackActivity implements OnViewClickListener<Contact> {
    private final static int REQUEST_ADD = 100;
    private final static int REQUEST_EDIT = 101;

    private ActivityAddressBinding mBinding;
    private ContactAdapter adapter;
    private boolean mDataChanged = false;

    public static Intent newIntent() {
        return new Intent(AppInstance.getInstance(), ContactActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_address);
        initLayout();
    }

    private void initLayout() {
        mBinding.tvToolbarTitle.setText(R.string.manage_contact);
        mBinding.btnBottom.setText(R.string.add_contact);

        mBinding.vList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.vList.addItemDecoration(new DecorationSpace(10));
        adapter = new ContactAdapter();
        adapter.setOnViewClickListener(this);
        mBinding.vList.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        ContactParam param = new ContactParam();
        BaseRequest<ContactParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<ArrayList<Contact>>>() {}.getType();
                DataResponse<ArrayList<Contact>> obj = GsonSingleton.get().fromJson(response, type);
                if (obj.data != null && !obj.data.isEmpty()) {
                    adapter.clear();
                    adapter.addAll(obj.data);
                }
                adapter.setLoaded();
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                adapter.setLoaded();
            }
        });
    }

    public void onClickAdd(View view) {
        startActivityForResult(ContactEditActivity.newIntent(), REQUEST_ADD);
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
    public void onViewClick(View view, Contact item, int position) {
        if (R.id.tv_edit == view.getId()) {
            edit(item);
        } else if (R.id.tv_delete == view.getId()) {
            delete(item);
        }
    }

    private void edit(Contact item) {
        startActivityForResult(ContactEditActivity.newIntent(item), REQUEST_EDIT);
    }

    private void delete(final Contact item) {
        ContactDelParam param = new ContactDelParam();
        param.vaid = item.vaid;
        BaseRequest<ContactDelParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                int position = adapter.getPosition(item);
                adapter.removeItem(position);
                mDataChanged = true;
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
