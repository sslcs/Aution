package com.happy.auction.module.order;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.base.BaseBackActivity;
import com.happy.auction.databinding.ActivityBaskBinding;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.BaskParam;
import com.happy.auction.entity.param.UploadParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.UploadInfo;
import com.happy.auction.glide.ImageLoader;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.ui.LoadingDialog;
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
 * 消息详情页面
 *
 * @author LiuCongshan
 */
public class BaskActivity extends BaseBackActivity {
    private static final String KEY_SID = "KEY_SID";
    private static final int REQUEST_CODE_CHOOSE = 100;

    private LoadingDialog mLoadingDialog;
    private ActivityBaskBinding mBinding;
    private int mSID;
    private ArrayList<String> mUploadImages = new ArrayList<>(2);
    private ArrayList<String> mSelectImages = new ArrayList<>(2);

    public static Intent newIntent(int sid) {
        Intent intent = new Intent(AppInstance.getInstance(), BaskActivity.class);
        intent.putExtra(KEY_SID, sid);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_bask);
        initLayout();
    }

    private void initLayout() {
        mBinding.setActivity(this);
        mSID = getIntent().getIntExtra(KEY_SID, -1);
    }

    public void onClickAlbum(View view) {
        MultiImageSelector.create()
                .multi()
                .count(2)
                .start(this, REQUEST_CODE_CHOOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (REQUEST_CODE_CHOOSE == requestCode) {
            List<String> pathList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            if (pathList != null && !pathList.isEmpty()) {
                mSelectImages.clear();
                mSelectImages.addAll(pathList);
                showImage();
                checkValid();
            }
        }
    }

    private void showImage() {
        mBinding.ivDelete0.setVisibility(View.VISIBLE);
        mBinding.ivSelected1.setVisibility(View.VISIBLE);
        ImageLoader.displayImage(mBinding.ivSelected0, mSelectImages.get(0));
        if (mSelectImages.size() > 1) {
            mBinding.ivDelete1.setVisibility(View.VISIBLE);
            ImageLoader.displayImage(mBinding.ivSelected1, mSelectImages.get(1));
        }
    }

    private void upload(final UploadInfo info, final String path) {
        UploadManager manager = new UploadManager();
        manager.put(path, info.key, info.token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo result, JSONObject response) {
                if (result.isOK()) {
                    mUploadImages.add(info.domain + "/" + info.key);
                    if (mUploadImages.size() == mSelectImages.size()) {
                        bask();
                    }
                } else {
                    ToastUtil.show(result.error);
                    dismissDialog();
                }
            }
        }, null);
    }

    private void bask() {
        BaskParam param = new BaskParam();
        param.sid = mSID;
        param.content = mBinding.etContent.getText().toString();
        param.images = mUploadImages;
        BaseRequest<BaskParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                dismissDialog();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                ToastUtil.show(message);
                dismissDialog();
            }
        });
    }

    public void onClickSend(View view) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog();
        }
        mLoadingDialog.show(getSupportFragmentManager(), "loading");

        mUploadImages.clear();
        UploadParam param = new UploadParam();
        param.amount = mSelectImages.size();
        param.type = UploadParam.TYPE_BASK;
        BaseRequest<UploadParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<ArrayList<UploadInfo>>>() {}.getType();
                DataResponse<ArrayList<UploadInfo>> obj = GsonSingleton.get().fromJson(response, type);
                if (obj.data == null || obj.data.isEmpty()) {
                    return;
                }

                int size = obj.data.size();
                for (int i = 0; i < size; i++) {
                    upload(obj.data.get(i), mSelectImages.get(i));
                }
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                ToastUtil.show(message);
                dismissDialog();
            }
        });
    }

    public void onClickDelete(View view) {
        if (view.getId() == R.id.iv_delete_0) {
            mSelectImages.remove(0);
            if (mSelectImages.isEmpty()) {
                mBinding.ivDelete0.setVisibility(View.GONE);
                mBinding.ivSelected0.setImageResource(R.drawable.ic_bask_add);
                mBinding.ivSelected1.setVisibility(View.GONE);
            } else {
                ImageLoader.displayImage(mBinding.ivSelected0, mSelectImages.get(0));
                mBinding.ivDelete1.setVisibility(View.GONE);
                mBinding.ivSelected1.setImageResource(R.drawable.ic_bask_add);
            }
        } else {
            mSelectImages.remove(1);
            mBinding.ivDelete1.setVisibility(View.GONE);
            mBinding.ivSelected1.setImageResource(R.drawable.ic_bask_add);
        }
        checkValid();
    }

    public void afterTextChanged(Editable s) {
        checkValid();
    }

    private void checkValid() {
        String content = mBinding.etContent.getText().toString();
        if (content.length() < 10 || mSelectImages.isEmpty()) {
            return;
        }

        mBinding.tvTitleRight.setEnabled(true);
    }

    private void dismissDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
}
