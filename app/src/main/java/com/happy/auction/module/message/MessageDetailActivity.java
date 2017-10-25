package com.happy.auction.module.message;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.base.BaseActivity;
import com.happy.auction.databinding.ActivityMessageDetailBinding;
import com.happy.auction.entity.item.ItemMessage;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.MessageDetailParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.MessageDetail;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;

import java.lang.reflect.Type;

/**
 * 消息详情页面
 *
 * @author LiuCongshan
 */
public class MessageDetailActivity extends BaseActivity {
    private static final String KEY_ITEM = "KEY_ITEM";

    private ActivityMessageDetailBinding mBinding;
    private MessageDetail mData;

    public static Intent newIntent(ItemMessage item) {
        Intent intent = new Intent(AppInstance.getInstance(), MessageDetailActivity.class);
        intent.putExtra(KEY_ITEM, item);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_message_detail);
        initLayout();
    }

    private void initLayout() {
        mBinding.tvContent.setMovementMethod(new ScrollingMovementMethod());

        ItemMessage mItem = (ItemMessage) getIntent().getSerializableExtra(KEY_ITEM);
        mData = new MessageDetail(mItem);
        mBinding.setData(mData);

        loadData();
    }

    private void loadData() {
        MessageDetailParam param = new MessageDetailParam();
        param.mid = mData.mid;
        BaseRequest<MessageDetailParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<MessageDetail>>() {}.getType();
                DataResponse<MessageDetail> obj = GsonSingleton.get().fromJson(response, type);
                mData = obj.data;
                mBinding.setData(obj.data);
            }
        });
    }
}
