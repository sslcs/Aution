package com.happy.auction.module.me;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.DecorationSpace;
import com.happy.auction.adapter.LoadMoreListener;
import com.happy.auction.base.BaseActivity;
import com.happy.auction.databinding.ActivityCardBinding;
import com.happy.auction.entity.item.ItemCard;
import com.happy.auction.entity.item.ItemCardPassword;
import com.happy.auction.entity.param.BaseParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.CardParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.ToastUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 我的卡密界面
 *
 * @author LiuCongshan
 * @date 17-10-20
 */

public class CardActivity extends BaseActivity {
    private final ObservableInt mSelectedCount = new ObservableInt();
    private ActivityCardBinding mBinding;
    private CardAdapter mAdapter;
    private int mStart = 0;
    private ArrayList<ItemCardPassword> mSelectedList = new ArrayList<>();
    private ArrayList<ItemCard> mData = new ArrayList<>(10);

    public static Intent newIntent() {
        return new Intent(AppInstance.getInstance(), CardActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_card);
        initLayout();
    }

    private void initLayout() {
        mBinding.setActivity(this);
        mBinding.setCount(mSelectedCount);

        mBinding.refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mStart = 0;
                mAdapter.clear();
                mData.clear();
                mSelectedCount.set(0);
                mSelectedList.clear();
                loadData();
            }
        });
        mBinding.vList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.vList.addItemDecoration(new DecorationSpace(10));
        mAdapter = new CardAdapter();
        mAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                loadData();
            }
        });
        mAdapter.setOnSelectionChangedListener(new CardAdapter.OnSelectionChangeListener() {
            @Override
            public void onSelectionChanged(ItemCard item, boolean selected) {
                boolean checkBottom = true;
                for (ItemCard itemCard : mData) {
                    if (!itemCard.isSelected) {
                        checkBottom = false;
                        break;
                    }
                }
                mBinding.tvSelect.setSelected(checkBottom);
            }

            @Override
            public void onChildSelectionChanged(ItemCardPassword item, boolean selected) {
                if (selected) {
                    mSelectedList.add(item);
                } else {
                    mSelectedList.remove(item);
                }
                mSelectedCount.set(mSelectedList.size());
            }
        });
        mBinding.vList.setAdapter(mAdapter);

        loadData();
    }

    private void loadData() {
        CardParam param = new CardParam();
        param.start = mStart;
        BaseRequest<CardParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                mBinding.refreshView.setRefreshing(false);
                Type type = new TypeToken<DataResponse<ArrayList<ItemCard>>>() {}.getType();
                DataResponse<ArrayList<ItemCard>> obj = GsonSingleton.get().fromJson(response, type);
                int size = 0;
                if (obj.data != null && !obj.data.isEmpty()) {
                    size = obj.data.size();
                    mData.addAll(obj.data);
                    mAdapter.addAll(obj.data);
                    mStart += size;
                }
                mAdapter.setHasMore(size >= BaseParam.DEFAULT_LIMIT);
                mAdapter.setLoaded();
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                mAdapter.setLoaded();
            }
        });
    }

    public void onClickCopy(View view) {
        if (mSelectedList.isEmpty()) {
            return;
        }

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Activity.CLIPBOARD_SERVICE);
        if (clipboard == null) {
            return;
        }

        String data = mSelectedList.toString();
        data = data.substring(1, data.lastIndexOf("/]"));
        data = data.replaceAll("/,", " ");
        ClipData clip = ClipData.newPlainText("text", data);
        clipboard.setPrimaryClip(clip);
        ToastUtil.show(R.string.tip_clipboard);
    }

    public void onClickSelect(View view) {
        mSelectedList.clear();
        if (mBinding.tvSelect.isSelected()) {
            mBinding.tvSelect.setSelected(false);
            for (ItemCard item : mData) {
                item.isSelected = false;
            }
        } else {
            mBinding.tvSelect.setSelected(true);
            for (ItemCard item : mData) {
                item.isSelected = true;
                mSelectedList.addAll(item.card);
            }
        }
        mSelectedCount.set(mSelectedList.size());
        mAdapter.notifyDataSetChanged();
    }

    public SpannableString getSelectedString(int count) {
        String text = getString(R.string.select_all, count);
        SpannableString ss = new SpannableString(text);
        int color = getResources().getColor(R.color.text_dark);
        ss.setSpan(new ForegroundColorSpan(color), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(14, true), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
