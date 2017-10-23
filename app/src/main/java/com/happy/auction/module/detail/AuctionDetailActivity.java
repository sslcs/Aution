package com.happy.auction.module.detail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.DecorationSpace;
import com.happy.auction.adapter.LoadMoreListener;
import com.happy.auction.base.BaseActivity;
import com.happy.auction.databinding.ActivityAuctionDetailBinding;
import com.happy.auction.entity.event.AuctionEndEvent;
import com.happy.auction.entity.event.BidEvent;
import com.happy.auction.entity.item.BaseGoods;
import com.happy.auction.entity.item.BidRecord;
import com.happy.auction.entity.item.ItemBask;
import com.happy.auction.entity.item.ItemGoods;
import com.happy.auction.entity.item.ItemPrevious;
import com.happy.auction.entity.param.AuctionCancelParam;
import com.happy.auction.entity.param.AuctionCoinParam;
import com.happy.auction.entity.param.AuctionDetailParam;
import com.happy.auction.entity.param.BalanceParam;
import com.happy.auction.entity.param.BaseParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.BidParam;
import com.happy.auction.entity.param.DetailBaskParam;
import com.happy.auction.entity.param.PreviousParam;
import com.happy.auction.entity.response.AuctionCoin;
import com.happy.auction.entity.response.AuctionDetail;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.UserBalance;
import com.happy.auction.module.login.LoginActivity;
import com.happy.auction.module.pay.AuctionPayActivity;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.DebugLog;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.RxBus;
import com.happy.auction.utils.ToastUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;

/**
 * 竞拍详情
 */
public class AuctionDetailActivity extends BaseActivity {
    private static final String KEY_GOODS = "GOODS";
    private static final int REQUEST_CODE_LOGIN = 100;
    private static final int REQUEST_CODE_PAY = 101;

    private final ObservableInt mTimes = new ObservableInt(1);
    private ActivityAuctionDetailBinding mBinding;
    private AuctionDetailBidAdapter mAdapter;
    private AuctionDetail mData;
    private AuctionCoin mAuctionCoin;

    private int indexPrevious = 0;
    private int indexBask = 0;
    private PreviousAdapter adapterPrevious;
    private BaskAdapter adapterBask;

    public static Intent newIntent(BaseGoods goods) {
        Intent intent = new Intent(AppInstance.getInstance(), AuctionDetailActivity.class);
        intent.putExtra(KEY_GOODS, goods);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_auction_detail);
        initLayout();
    }

    private void initLayout() {
        mBinding.tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        mBinding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                showBlackTitle(verticalOffset);
            }
        });

        mAdapter = new AuctionDetailBidAdapter();
        mBinding.listRecord.setAdapter(mAdapter);
        DecorationSpace decoration = new DecorationSpace();
        decoration.enableHeader();
        mBinding.listRecord.addItemDecoration(decoration);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mBinding.listRecord.setLayoutManager(manager);

        adapterPrevious = new PreviousAdapter();
        adapterPrevious.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                loadPrevious();
            }
        });
        adapterBask = new BaskAdapter();
        adapterBask.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                loadBask();
            }
        });
        mBinding.vList.addItemDecoration(decoration);
        mBinding.vList.setAdapter(adapterPrevious);

        BaseGoods goods = (BaseGoods) getIntent().getSerializableExtra(KEY_GOODS);
        if (goods instanceof ItemGoods) {
            mData = new AuctionDetail((ItemGoods) goods);
        } else {
            mData = new AuctionDetail(goods);
        }

        mBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    mBinding.vList.setAdapter(adapterBask);
                    if (adapterBask.isEmpty()) {
                        loadBask();
                    }
                } else {
                    mBinding.vList.setAdapter(adapterPrevious);
                    if (adapterPrevious.isEmpty()) {
                        loadPrevious();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        initData();
        listenEvents();
        loadData();
        loadAuctionCoin();
    }

    private void showBlackTitle(int verticalOffset) {
        int offset = -mBinding.tvAuctionStatus.getTop();
        if (verticalOffset > offset) {
            mBinding.tvToolbarPrice.setVisibility(View.GONE);
            mBinding.tvToolbarTime.setVisibility(View.GONE);
            return;
        }

        mBinding.tvToolbarPrice.setVisibility(View.VISIBLE);
        mBinding.tvToolbarTime.setVisibility(View.VISIBLE);
        int height = offset - verticalOffset;
        ConstraintLayout.LayoutParams paramsPrice = (ConstraintLayout.LayoutParams) mBinding.tvToolbarPrice.getLayoutParams();
        ConstraintLayout.LayoutParams paramsTime = (ConstraintLayout.LayoutParams) mBinding.tvToolbarTime.getLayoutParams();
        int heightTitle = mBinding.tvToolbarTitle.getHeight();
        if (height < heightTitle) {
            paramsPrice.height = height;
            paramsTime.height = height;
        } else {
            paramsPrice.height = heightTitle;
            paramsTime.height = heightTitle;
        }
        mBinding.tvToolbarPrice.setLayoutParams(paramsPrice);
        mBinding.tvToolbarTime.setLayoutParams(paramsTime);
    }

    private void initData() {
        mBinding.setActivity(this);
        mBinding.setBidTimes(mTimes);
        mBinding.setData(mData);
        mBinding.tvAuctionStatus.setSyncView(mBinding.tvToolbarTime);
        if (mData.status == 0) {
            mBinding.tvAuctionStatus.finish();
        } else {
            mBinding.tvAuctionStatus.setExpireTime(mData.bid_expire_time);
            mBinding.tvAuctionStatus.setRepeat(mData.current_price == mData.bid_start_price);
        }

        if (mData.bid_records != null && !mData.bid_records.isEmpty()) {
            mAdapter.addAll(mData.bid_records);
            mBinding.setNewBid(mData.bid_records.get(0));
            setBtnMoreVisibility();
        }
    }

    private void setBtnMoreVisibility() {
        mBinding.tvMore.setVisibility(mAdapter.getRealCount() > 3 ? View.VISIBLE : View.GONE);
    }

    private void listenEvents() {
        RxBus.getDefault().subscribe(this, BidEvent.class, new Consumer<BidEvent>() {
            @Override
            public void accept(BidEvent event) throws Exception {
                if (event.sid != mData.sid || event.current_price <= mData.current_price) {
                    return;
                }
                mData.setCurrentPrice(event.current_price);
                mBinding.tvAuctionStatus.setExpireTime(event.bid_expire_time);
                mBinding.tvAuctionStatus.setRepeat(false);

                BidRecord record = new BidRecord(event);
                if (TextUtils.isEmpty(record.uid)) {
                    return;
                }
                mAdapter.addItem(record);
                mBinding.setNewBid(record);
                setBtnMoreVisibility();

                if (!record.uid.equals(AppInstance.getInstance().uid)) {
                    return;
                }
                if (mAuctionCoin.current_bid_coins > 0) {
                    int progress = mAuctionCoin.current_bidden_coins + 1;
                    if (progress == mAuctionCoin.current_bid_coins) {
                        mAuctionCoin.setCurrentProgress(0);
                        mAuctionCoin.setCurrentCoin(0);
                    } else {
                        mAuctionCoin.setCurrentProgress(progress);
                    }
                }
            }
        });

        RxBus.getDefault().subscribe(this, AuctionEndEvent.class, new Consumer<AuctionEndEvent>() {
            @Override
            public void accept(AuctionEndEvent event) throws Exception {
                if (event.sid != mData.sid) {
                    return;
                }
                mData.setStatus(0);
                mBinding.tvAuctionStatus.finish();
                loadAuctionCoin();
            }
        });
    }

    private void loadData() {
        AuctionDetailParam param = new AuctionDetailParam();
        param.sid = mData.sid;
        BaseRequest<AuctionDetailParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<AuctionDetail>>() {}.getType();
                DataResponse<AuctionDetail> obj = GsonSingleton.get().fromJson(response, type);
                mData = obj.data;
                initData();
                loadPrevious();
            }
        });
    }

    private void loadLatest() {
        AuctionDetailParam param = new AuctionDetailParam();
        param.gid = mData.gid;
        BaseRequest<AuctionDetailParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<AuctionDetail>>() {}.getType();
                DataResponse<AuctionDetail> obj = GsonSingleton.get().fromJson(response, type);
                mData = obj.data;
                initData();
                loadAuctionCoin();
            }
        });
    }

    private void loadAuctionCoin() {
        AuctionCoinParam param = new AuctionCoinParam();
        param.sid = mData.sid;
        BaseRequest<AuctionCoinParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<AuctionCoin>>() {}.getType();
                DataResponse<AuctionCoin> obj = GsonSingleton.get().fromJson(response, type);
                mAuctionCoin = obj.data;
                mBinding.setCoin(mAuctionCoin);
            }
        });
    }

    public void onClickBid(View view) {
        if (!AppInstance.getInstance().isLogin()) {
            startActivityForResult(LoginActivity.newIntent(), REQUEST_CODE_LOGIN);
            return;
        }

        getBalance();
    }

    private void bid() {
        if (mTimes.get() == 0) {
            mTimes.set(1);
        }
        final int count = mTimes.get();
        if (AppInstance.getInstance().getBalance() < count) {
            Intent pay = AuctionPayActivity.newIntent(this, mData.getItemGoods(), count);
            startActivityForResult(pay, REQUEST_CODE_PAY);
            return;
        }

        BidParam param = new BidParam();
        param.sid = mData.sid;
        param.buy = mTimes.get();
        param.take_coin = param.buy;
        BaseRequest<BidParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                onPaySuccess(count);
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                ToastUtil.show(message);
            }
        });
    }

    private void onPaySuccess(int count) {
        if (AppInstance.getInstance().getUser().free_coin >= count) {
            mAuctionCoin.setBidGiftCoin(mAuctionCoin.bid_gift_coin + count);
            if (count > 1) {
                mAuctionCoin.setCurrentProgress(0);
                mAuctionCoin.setCurrentCoin(count);
            }
        } else {
            loadAuctionCoin();
        }
    }

    public void onClickNext(View view) {
        mAdapter.clear();
        mBinding.setCoin(null);
        mBinding.setNewBid(null);
        setBtnMoreVisibility();
        loadLatest();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mData.status != 0) {
            mBinding.tvAuctionStatus.cancel();
        }
        RxBus.getDefault().unsubscribe(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (REQUEST_CODE_LOGIN == requestCode) {
            getBalance();
        } else if (REQUEST_CODE_PAY == requestCode) {
            onPaySuccess(mTimes.get());
        }
    }

    public void onClickMinus(View view) {
        if (mTimes.get() > 1) {
            mTimes.set(mTimes.get() - 1);
        }
    }

    public void onClickPlus(View view) {
        mTimes.set(mTimes.get() + 1);
    }

    public void afterTextChanged(Editable editable) {
        try {
            int number = Integer.parseInt(editable.toString());
            mTimes.set(number);
        } catch (NumberFormatException e) {
            mTimes.set(0);
        }
    }

    public void onClickCancel(View view) {
        AuctionCancelParam param = new AuctionCancelParam();
        param.sid = mData.sid;
        BaseRequest<AuctionCancelParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                mAuctionCoin.setCurrentCoin(0);
                loadAuctionCoin();
            }
        });
    }

    public void onClickAutoBid(View view) {}

    private void loadPrevious() {
        PreviousParam param = new PreviousParam();
        param.gid = mData.gid;
        param.start = indexPrevious;
        BaseRequest<PreviousParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                adapterPrevious.setLoaded();
                Type type = new TypeToken<DataResponse<ArrayList<ItemPrevious>>>() {}.getType();
                DataResponse<ArrayList<ItemPrevious>> obj = GsonSingleton.get().fromJson(response, type);
                int size = 0;
                if (obj.data != null && !obj.data.isEmpty()) {
                    size = obj.data.size();
                    adapterPrevious.addAll(obj.data);
                    indexPrevious += size;
                }
                adapterPrevious.setHasMore(size >= BaseParam.DEFAULT_LIMIT);
            }
        });
    }

    private void loadBask() {
        DetailBaskParam param = new DetailBaskParam();
        param.gid = mData.gid;
        param.start = indexBask;
        BaseRequest<DetailBaskParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                adapterBask.setLoaded();
                Type type = new TypeToken<DataResponse<ArrayList<ItemBask>>>() {}.getType();
                DebugLog.e("response : " + response);
                DataResponse<ArrayList<ItemBask>> obj = GsonSingleton.get().fromJson(response, type);
                int size = 0;
                if (obj.data != null && !obj.data.isEmpty()) {
                    adapterBask.addAll(obj.data);
                    size = obj.data.size();
                    indexBask += size;
                }
                adapterBask.setHasMore(size >= BaseParam.DEFAULT_LIMIT);
            }
        });
    }

    public void onClickMore(View view) {
        startActivity(BidRecordActivity.newIntent(mData.sid, mData.status));
    }

    private void getBalance() {
        BalanceParam data = new BalanceParam();
        BaseRequest<BalanceParam> request = new BaseRequest<>(data);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<UserBalance>>() {}.getType();
                DataResponse<UserBalance> obj = GsonSingleton.get().fromJson(response, type);
                if (obj.data != null) {
                    AppInstance.getInstance().setBalance(obj.data);
                }
                bid();
            }
        });
    }
}
