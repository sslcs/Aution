package com.happy.auction.module.detail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.LoadMoreListener;
import com.happy.auction.base.BaseActivity;
import com.happy.auction.databinding.ActivityListBinding;
import com.happy.auction.entity.item.BidRecord;
import com.happy.auction.entity.param.BaseParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.BidRecordParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by LiuCongshan on 17-9-29.
 * 竞拍记录界面
 */

public class BidRecordActivity extends BaseActivity {
    private static final String KEY_SID = "SID";
    private static final String KEY_STATUS = "STATUS";

    private ActivityListBinding binding;
    private int sid;
    private int status;

    private BidRecordAdapter adapter;
    private int index = 0;

    public static Intent newIntent(int sid, int status) {
        Intent intent = new Intent(AppInstance.getInstance(), BidRecordActivity.class);
        intent.putExtra(KEY_SID, sid);
        intent.putExtra(KEY_STATUS, status);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list);
        initLayout();
    }

    private void initLayout() {
        binding.vList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BidRecordAdapter();
        adapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                loadData();
            }
        });
        binding.vList.setAdapter(adapter);

        sid = getIntent().getIntExtra(KEY_SID, 0);
        status = getIntent().getIntExtra(KEY_STATUS, 0);
        adapter.setStatus(status);
        loadData();
    }

    private void loadData() {
        BidRecordParam param = new BidRecordParam();
        param.sid = sid;
        param.start = index;
        BaseRequest<BidRecordParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                adapter.setLoaded();
                Type type = new TypeToken<DataResponse<ArrayList<BidRecord>>>() {}.getType();
                DataResponse<ArrayList<BidRecord>> obj = GsonSingleton.get().fromJson(response, type);
                int size = 0;
                if (obj.data != null && !obj.data.isEmpty()) {
                    size = obj.data.size();
                    adapter.addAll(obj.data);
                    index = adapter.getLast().bid;
                }
                adapter.setHasMore(size >= BaseParam.DEFAULT_LIMIT);
            }
        });
    }
}
