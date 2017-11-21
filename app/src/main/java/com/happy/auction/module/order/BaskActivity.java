package com.happy.auction.module.order;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ImageView;

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
import com.happy.auction.utils.EventAgent;
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
    private static final int MAX_COUNT = 4;

    private LoadingDialog mLoadingDialog;
    private ActivityBaskBinding mBinding;
    private int mSID;
    private ArrayList<String> mUploadImages = new ArrayList<>(MAX_COUNT);
    private ArrayList<String> mSelectImages = new ArrayList<>(MAX_COUNT);
    private boolean bSendEvent = true;
    private ImageView[] ivArraySelect, ivArrayDelete;

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

        ivArraySelect = new ImageView[]{mBinding.ivSelected0, mBinding.ivSelected1, mBinding.ivSelected2, mBinding.ivSelected3};
        ivArrayDelete = new ImageView[]{mBinding.ivDelete0, mBinding.ivDelete1, mBinding.ivDelete2, mBinding.ivDelete3};
        for (int i = 0; i < MAX_COUNT; i++) {
            ivArrayDelete[i].setTag(i);
        }
    }

    public void onClickAlbum(View view) {
        EventAgent.onEvent(R.string.share_order_photo);
        MultiImageSelector.create()
                .multi()
                .count(4)
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
        int size = mSelectImages.size();
        for (int i = 0; i < MAX_COUNT; i++) {
            if (i < size) {
                ivArrayDelete[i].setVisibility(View.VISIBLE);
                ImageLoader.displayImage(ivArraySelect[i], mSelectImages.get(i));
                if (i < MAX_COUNT - 1) {
                    ivArraySelect[i + 1].setVisibility(View.VISIBLE);
                }
            } else if (i == size) {
                ivArrayDelete[i].setVisibility(View.GONE);
                ivArraySelect[i].setImageResource(R.drawable.ic_bask_add);
            } else {
                ivArrayDelete[i].setVisibility(View.GONE);
                ivArraySelect[i].setVisibility(View.GONE);
            }
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
        EventAgent.onEvent(R.string.share_order_send);
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
        int position = (int) view.getTag();
        mSelectImages.remove(position);
        showImage();
        checkValid();
    }

    public void afterTextChanged(Editable s) {
        if (bSendEvent) {
            bSendEvent = false;
            EventAgent.onEvent(R.string.share_order_content);
        }
        checkValid();
    }

    private void checkValid() {
        String content = mBinding.etContent.getText().toString();
        mBinding.tvTitleRight.setEnabled(content.length() > 0 && !mSelectImages.isEmpty());
    }

    private void dismissDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
}
