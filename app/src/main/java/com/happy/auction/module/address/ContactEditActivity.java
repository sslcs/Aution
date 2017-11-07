package com.happy.auction.module.address;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.base.BaseBackActivity;
import com.happy.auction.databinding.ActivityContactEditBinding;
import com.happy.auction.entity.item.Contact;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.ContactEditParam;
import com.happy.auction.entity.response.AddContactResponse;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.ToastUtil;

import java.lang.reflect.Type;

/**
 * 联系人编辑界面
 *
 * @author LiuCongshan
 * @date 17-10-17
 */

public class ContactEditActivity extends BaseBackActivity {
    private static final String KEY_DATA = "DATA";
    private ActivityContactEditBinding mBinding;
    private Contact mData;

    public static Intent newIntent() {
        return new Intent(AppInstance.getInstance(), ContactEditActivity.class);
    }

    public static Intent newIntent(Contact contact) {
        Intent intent = newIntent();
        intent.putExtra(KEY_DATA, contact);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_contact_edit);

        initLayout();
    }

    private void initLayout() {
        if (getIntent().hasExtra(KEY_DATA)) {
            mData = (Contact) getIntent().getSerializableExtra(KEY_DATA);
            mBinding.setData(mData);
            mBinding.tvToolbarTitle.setText(R.string.edit_contact);
        } else {
            mBinding.tvToolbarTitle.setText(R.string.title_add_contact);
        }
    }

    public void onClickStore(View view) {
        ContactEditParam param = new ContactEditParam(mData != null);
        param.remark = mBinding.etUsername.getText().toString().trim();
        param.phone = mBinding.etPhone.getText().toString().trim();
        if (mData != null) {
            param.vaid = mData.vaid;
        }
        BaseRequest<ContactEditParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<AddContactResponse>>() {}.getType();
                DataResponse<AddContactResponse> obj = GsonSingleton.get().fromJson(response, type);
                Intent data = new Intent();
                if (obj != null && obj.data != null) {
                    data.putExtra(ContactSelectActivity.EXTRA_CONTACT_ID, obj.data.vaid);
                }
                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                ToastUtil.show(message);
            }
        });
    }
}
