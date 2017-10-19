package com.happy.auction.module.address;

import android.content.Intent;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.OnItemClickListener;
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
public class ContactSelectActivity extends BaseAddressSelectActivity {
    public static final String EXTRA_CONTACT_ID = "contact_id";
    private ContactSelectAdapter adapter;

    public static Intent newIntent() {
        return new Intent(AppInstance.getInstance(), ContactSelectActivity.class);
    }

    @Override
    protected void initChildLayout() {
        mBinding.tvToolbarTitle.setText(R.string.select_contact);

        adapter = new ContactSelectAdapter();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                adapter.setSelectPosition(position);
            }
        });
        mBinding.vList.setAdapter(adapter);
    }

    @Override
    protected void loadData() {
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

    @Override
    protected Intent getManageIntent() {
        return ContactActivity.newIntent();
    }

    @Override
    protected void putIntentData(Intent intent) {
        intent.putExtra(EXTRA_CONTACT_ID, adapter.getItem(adapter.getSelectPosition()).vaid);
    }
}
