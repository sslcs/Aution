package com.happy.auction.module.message;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.adapter.DecorationSpace;
import com.happy.auction.adapter.LoadMoreListener;
import com.happy.auction.adapter.OnItemClickListener;
import com.happy.auction.base.BaseFragment;
import com.happy.auction.databinding.FragmentListBinding;
import com.happy.auction.entity.item.ItemMessage;
import com.happy.auction.entity.param.BaseParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.MessageParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 消息记录
 *
 * @author LiuCongshan
 */
public class MessageFragment extends BaseFragment {
    public final static int TYPE_AUCTION = 1;
    public final static int TYPE_DELIVERY = 2;
    public final static int TYPE_NOTICE = 3;

    private static final String KEY_TYPE = "type";

    private FragmentListBinding mBinding;
    private MessageAdapter mAdapter;
    private int mIndex = 0;

    /**
     * 消息类型， 1 竞拍消息， 2 物流信息， 3 系统公告， 不传时为获取所有
     *
     * @param type {@link #TYPE_AUCTION}, {@link #TYPE_DELIVERY}, {@link #TYPE_NOTICE}
     * @return fragment实例
     */
    public static MessageFragment newInstance(int type) {
        MessageFragment fragment = new MessageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        mBinding = FragmentListBinding.inflate(inflater);
        initLayout();
        return mBinding.getRoot();
    }

    private void initLayout() {
        mBinding.vList.setLayoutManager(new LinearLayoutManager(getActivity()));
        DecorationSpace decoration = new DecorationSpace(5);
        decoration.enableHeader();
        mBinding.vList.addItemDecoration(decoration);
        mAdapter = new MessageAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener<ItemMessage>() {
            @Override
            public void onItemClick(View view, ItemMessage item, int position) {
                startActivity(MessageDetailActivity.newIntent(item));
                if (item.is_read == 0) {
                    item.is_read = 1;
                    AppInstance.getInstance().minusMessageCount();
                    mAdapter.notifyItemChanged(position);
                }
            }
        });
        mAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                loadData();
            }
        });
        mBinding.vList.setAdapter(mAdapter);

        if (getUserVisibleHint()) {
            loadData();
        }
    }

    private void loadData() {
        MessageParam param = new MessageParam();
        param.start = mIndex;
        param.type = getArguments().getInt(KEY_TYPE);
        BaseRequest<MessageParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                mAdapter.setLoaded();
                Type type = new TypeToken<DataResponse<ArrayList<ItemMessage>>>() {}.getType();
                DataResponse<ArrayList<ItemMessage>> obj = GsonSingleton.get().fromJson(response, type);
                int size = 0;
                if (obj.data != null && !obj.data.isEmpty()) {
                    mAdapter.addAll(obj.data);
                    size = obj.data.size();
                    mIndex = mAdapter.getLast().mid;
                }
                mAdapter.setHasMore(size >= BaseParam.DEFAULT_LIMIT);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (hasCreatedView && isVisibleToUser && mAdapter.isEmpty()) {
            loadData();
        }
    }
}
