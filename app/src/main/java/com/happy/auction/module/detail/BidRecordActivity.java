package com.happy.auction.module.detail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.DecorationSpace;
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
 * 竞拍记录界面
 *
 * @author LiuCongshan
 * @date 17-9-29
 */

public class BidRecordActivity extends BaseActivity {
    private static final String KEY_SID = "SID";
    private static final String KEY_STATUS = "STATUS";

    private ActivityListBinding mBinding;
    private int mSid;

    private BidRecordAdapter mAdapter;
    private int mStart = 0;

    /**
     * 返回竞拍记录页面的intent
     *
     * @param sid    商品id
     * @param status 商品状态 0:已结束，1：未开始，2：正在进行
     * @return intent for this activity
     */
    public static Intent newIntent(int sid, int status) {
        Intent intent = new Intent(AppInstance.getInstance(), BidRecordActivity.class);
        intent.putExtra(KEY_SID, sid);
        intent.putExtra(KEY_STATUS, status);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_list);
        initLayout();
    }

    private void initLayout() {
        mBinding.vList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.vList.addItemDecoration(new DecorationSpace());
        mAdapter = new BidRecordAdapter();
        mAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                loadData();
            }
        });
        mBinding.vList.setAdapter(mAdapter);

        mBinding.refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mStart = 0;
                loadData();
            }
        });

        mSid = getIntent().getIntExtra(KEY_SID, 0);
        int status = getIntent().getIntExtra(KEY_STATUS, 0);
        mAdapter.setStatus(status);
        loadData();
    }

    private void loadData() {
        BidRecordParam param = new BidRecordParam();
        param.sid = mSid;
        param.start = mStart;
        BaseRequest<BidRecordParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                mBinding.refreshView.setRefreshing(false);
                if (mStart == 0) {
                    mAdapter.clear();
                }
                Type type = new TypeToken<DataResponse<ArrayList<BidRecord>>>() {}.getType();
                DataResponse<ArrayList<BidRecord>> obj = GsonSingleton.get().fromJson(response, type);
                int size = 0;
                if (obj.data != null && !obj.data.isEmpty()) {
                    size = obj.data.size();
                    mAdapter.addAll(obj.data);
                    mStart = mAdapter.getLast().bid;
                }
                mAdapter.setHasMore(size >= BaseParam.DEFAULT_LIMIT);
                mAdapter.setLoaded();
            }
        });
    }
}
