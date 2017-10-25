package com.happy.auction.module.me;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.base.BaseActivity;
import com.happy.auction.databinding.ActivityManagerBinding;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.UpdateUserInfoParam;
import com.happy.auction.entity.param.UploadParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.UploadInfo;
import com.happy.auction.glide.ImageLoader;
import com.happy.auction.module.address.AddressActivity;
import com.happy.auction.module.address.ContactActivity;
import com.happy.auction.module.login.ChangePasswordActivity;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.ToastUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 账号管理页面
 *
 * @author LiuCongshan
 */
public class ManagerActivity extends BaseActivity {
    private static final int REQUEST_USER_INFO = 100;
    private static final int REQUEST_CODE_CHOOSE = 101;

    private ActivityManagerBinding mBinding;

    public static Intent newIntent(Context context) {
        return new Intent(context, ManagerActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_manager);
        initLayout();
    }

    private void initLayout() {
        mBinding.setUser(AppInstance.getInstance().getUser());
    }

    public void onClickAvatar(View view) {
        MultiImageSelector.create()
                .showCamera(true)
                .single()
                .start(this, REQUEST_CODE_CHOOSE);
    }

    public void onClickPassword(View view) {startActivity(ChangePasswordActivity.newIntent());}

    public void onClickAddress(View view) {
        startActivity(AddressActivity.newIntent());
    }

    public void onClickContact(View view) {
        startActivity(ContactActivity.newIntent());
    }

    public void onClickUsername(View view) {
        startActivityForResult(UsernameActivity.newIntent(this), REQUEST_USER_INFO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (REQUEST_USER_INFO == requestCode) {
            mBinding.setUser(AppInstance.getInstance().getUser());
        } else if (REQUEST_CODE_CHOOSE == requestCode) {
            List<String> pathList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            if (pathList != null && !pathList.isEmpty()) {
                String path = pathList.get(0);
                ImageLoader.loadAvatar(mBinding.ivAvatar, path);
                getUploadInfo(path);
            }
        }
    }

    private void getUploadInfo(final String path) {
        UploadParam param = new UploadParam();
        param.type = UploadParam.TYPE_AVATAR;
        BaseRequest<UploadParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<ArrayList<UploadInfo>>>() {}.getType();
                DataResponse<ArrayList<UploadInfo>> obj = GsonSingleton.get().fromJson(response, type);
                if (obj.data != null && !obj.data.isEmpty()) {
                    upload(obj.data.get(0), path);
                }
            }
        });
    }

    private void upload(final UploadInfo info, String path) {
        UploadManager manager = new UploadManager();
        manager.put(path, info.key, info.token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo result, JSONObject response) {
                if (result.isOK()) {
                    update(info);
                } else {
                    ToastUtil.show(result.error);
                }
            }
        }, null);
    }

    private void update(UploadInfo info) {
        final UpdateUserInfoParam param = new UpdateUserInfoParam();
        param.headimg = info.domain + "/" + info.key;
        BaseRequest<UpdateUserInfoParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                AppInstance.getInstance().setAvatar(param.headimg);
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                ToastUtil.show(message);
            }
        });
    }
}
