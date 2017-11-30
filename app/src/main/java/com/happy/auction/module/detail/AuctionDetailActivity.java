package com.happy.auction.module.detail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.DecorationSpace;
import com.happy.auction.adapter.LoadMoreListener;
import com.happy.auction.adapter.OnItemClickListener;
import com.happy.auction.base.BaseBackActivity;
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
import com.happy.auction.module.home.ImageActivity;
import com.happy.auction.module.login.LoginActivity;
import com.happy.auction.module.main.WebActivity;
import com.happy.auction.module.pay.AuctionPayActivity;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.EventAgent;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.RxBus;
import com.happy.auction.utils.ToastUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;


/**
 * 竞拍详情页面
 *
 * @author LiuCongshan
 */
public class AuctionDetailActivity extends BaseBackActivity {
    private static final String KEY_GOODS = "GOODS";
    private static final int REQUEST_CODE_PAY = 101;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private final ObservableInt mTimes = new ObservableInt(1);
    private ActivityAuctionDetailBinding mBinding;
    private AuctionDetailBidAdapter mAdapter;
    private AuctionDetail mData;
    private AuctionCoin mAuctionCoin;

    private int mIndexPrevious = 0;
    private int mIndexBask = 0;
    private PreviousAdapter mAdapterPrevious;
    private BaskAdapter mAdapterBask;
    private NumberPicker mNumberPicker;

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
                mBinding.refreshView.setEnabled(verticalOffset >= 0);
            }
        });
        mBinding.refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        mAdapter = new AuctionDetailBidAdapter();
        mBinding.listRecord.setAdapter(mAdapter);
        DecorationSpace decoration = new DecorationSpace();
        decoration.enableHeader();
        decoration.enableFooter();
        mBinding.listRecord.addItemDecoration(decoration);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mBinding.listRecord.setLayoutManager(manager);

        mAdapterPrevious = new PreviousAdapter();
        mAdapterPrevious.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                loadPrevious();
            }
        });
        mAdapterPrevious.setOnItemClickListener(new OnItemClickListener<ItemPrevious>() {
            @Override
            public void onItemClick(View view, ItemPrevious item, int position) {
                BaseGoods goods = mData.getBaseGoods();
                goods.sid = item.sid;
                startActivity(newIntent(goods));
            }
        });
        mAdapterBask = new BaskAdapter();
        mAdapterBask.setOnClickImageListener(new BaskAdapter.OnClickImageListener() {
            @Override
            public void onClick(ArrayList<String> img, int selection) {
                startActivity(ImageActivity.newIntent(img, selection));
            }
        });
        mAdapterBask.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                loadBask();
            }
        });
        mBinding.vList.addItemDecoration(decoration);
        mBinding.vList.setAdapter(mAdapterPrevious);

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
                    EventAgent.onEvent(R.string.goods_detail_before_share);
                    mBinding.vList.setAdapter(mAdapterBask);
                    if (mAdapterBask.isEmpty()) {
                        loadBask();
                    }
                } else {
                    EventAgent.onEvent(R.string.goods_detail_before);
                    mBinding.vList.setAdapter(mAdapterPrevious);
                    if (mAdapterPrevious.isEmpty()) {
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

        mBinding.etTimes.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom - bottom > view.getHeight()) {
                    EventAgent.onEvent(R.string.goods_detail_bid_choose);
                    showNumberPicker();
                } else if (mNumberPicker != null) {
                    mNumberPicker.dismiss();
                }
            }
        });

        RxView.clicks(mBinding.btnBid)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        onClickBid(null);
                    }
                });
        RxView.clicks(mBinding.btnCancel)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        onClickCancel(null);
                    }
                });

        initData();
        listenEvents();
        loadData();
        loadAuctionCoin();
    }

    private void showNumberPicker() {
        if (mNumberPicker == null) {
            mNumberPicker = new NumberPicker(AuctionDetailActivity.this, mTimes);
        }
        mNumberPicker.reset();
        int offsetX = AppInstance.getInstance().dp2px(25);
        int[] location = new int[2];
        mBinding.etTimes.getLocationOnScreen(location);
        int offsetY = getResources().getDisplayMetrics().heightPixels - location[1];
        mNumberPicker.showAtLocation(mBinding.getRoot(), Gravity.LEFT | Gravity.BOTTOM, offsetX, offsetY);
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
            mBinding.tvAuctionStatus.setExpireTime(mData.countdown);
            mBinding.tvAuctionStatus.setRepeat(mData.current_price == mData.bid_start_price);
        }

        mAdapter.clear();
        if (mData.bid_records != null && !mData.bid_records.isEmpty()) {
            mAdapter.addAll(mData.bid_records);
            mBinding.setNewBid(mData.bid_records.get(0));
        }
        setBtnMoreVisibility();
    }

    private void setBtnMoreVisibility() {
        int visible = mAdapter.getRealCount() > 3 ? View.VISIBLE : View.GONE;
        mBinding.tvMore.setVisibility(visible);
    }

    private void listenEvents() {
        RxBus.getDefault().subscribe(this, BidEvent.class, new Consumer<BidEvent>() {
            @Override
            public void accept(BidEvent event) throws Exception {
                if (event.sid != mData.sid || event.current_price <= mData.current_price) {
                    return;
                }
                mData.setCurrentPrice(event.current_price);
                mBinding.tvAuctionStatus.setExpireTime(event.countdown);
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
                    int progress = mAuctionCoin.current_bidden_coins + mData.bid_fee;
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
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(mBinding.etTimes.getWindowToken(), 0);
                }
                if (mNumberPicker != null) {
                    mNumberPicker.dismiss();
                }
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
                mBinding.refreshView.setRefreshing(false);
                Type type = new TypeToken<DataResponse<AuctionDetail>>() {}.getType();
                DataResponse<AuctionDetail> obj = GsonSingleton.get().fromJson(response, type);
                mData = obj.data;
                initData();

                if (mBinding.tabLayout.getSelectedTabPosition() == 0) {
                    mIndexPrevious = 0;
                    loadPrevious();
                } else {
                    mIndexBask = 0;
                    loadBask();
                }
            }
        });
    }

    public String getNotLogin() {
        return getString(R.string.not_login);
    }

    private void loadAuctionCoin() {
        if (!AppInstance.getInstance().isLogin()) {
            mBinding.tvBidTimes.setVisibility(View.VISIBLE);
            mBinding.tvLogin.setVisibility(View.VISIBLE);
            return;
        }

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
                mBinding.tvBidTimes.setVisibility(View.VISIBLE);
                mBinding.tvLogin.setVisibility(View.GONE);
            }
        });
    }

    public void onClickBid(View view) {
        EventAgent.onEvent(R.string.goods_detail_bid);
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mBinding.etTimes.getWindowToken(), 0);
        }

        if (AppInstance.getInstance().isLogin()) {
            getBalance();
            return;
        }
        startActivity(LoginActivity.newIntent(new LoginActivity.OnLoginListener() {
            @Override
            public void onLogin() {
                getBalance();
                loadAuctionCoin();
            }
        }));
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

    private void bid() {
        if (mTimes.get() == 0) {
            mTimes.set(1);
        }
        int count = mTimes.get();
        final int coin = count * mData.bid_fee;
        if (AppInstance.getInstance().getBalance() < coin) {
            Intent pay = AuctionPayActivity.newIntent(this, mData.getBaseGoods(), count);
            startActivityForResult(pay, REQUEST_CODE_PAY);
            return;
        }

        BidParam param = new BidParam();
        param.sid = mData.sid;
        param.buy = count;
        param.take_coin = coin;
        BaseRequest<BidParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                onPaySuccess(coin);
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                ToastUtil.show(message);
            }
        });
    }

    private void onPaySuccess(int coin) {
        if (AppInstance.getInstance().getUser().free_coin >= coin) {
            mAuctionCoin.setBidGiftCoin(mAuctionCoin.bid_gift_coin + coin);
            if (coin > mData.bid_fee) {
                mAuctionCoin.setCurrentProgress(0);
                mAuctionCoin.setCurrentCoin(coin);
            }
        } else {
            loadAuctionCoin();
        }
    }

    public void onClickNext(View view) {
        EventAgent.onEvent(R.string.goods_detail_bid_new);

        AuctionDetailParam param = new AuctionDetailParam();
        param.gid = mData.gid;
        BaseRequest<AuctionDetailParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                mBinding.setNewBid(null);
                Type type = new TypeToken<DataResponse<AuctionDetail>>() {}.getType();
                DataResponse<AuctionDetail> obj = GsonSingleton.get().fromJson(response, type);
                mData = obj.data;
                initData();
                loadAuctionCoin();
            }
        });
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

        if (REQUEST_CODE_PAY == requestCode) {
            onPaySuccess(mTimes.get());
        }
    }

    public void onClickMinus(View view) {
        EventAgent.onEvent(R.string.goods_detail_bid_add);
        if (mTimes.get() > 1) {
            mTimes.set(mTimes.get() - 1);
        }
        showNumberPicker();
    }

    public void onClickPlus(View view) {
        EventAgent.onEvent(R.string.goods_detail_bid_minus);
        mTimes.set(mTimes.get() + 1);
        showNumberPicker();
    }

    public void afterTextChanged(Editable editable) {
        try {
            int number = Integer.parseInt(editable.toString());
            mTimes.set(number);
        } catch (NumberFormatException e) {
            mBinding.etTimes.setText("0");
            mBinding.etTimes.setSelection(1);
        }
    }

    public void onClickCancel(View view) {
        EventAgent.onEvent(R.string.goods_detail_bid_cancel);
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
        param.start = mIndexPrevious;
        BaseRequest<PreviousParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                if (mIndexPrevious == 0) {
                    mAdapterPrevious.clear();
                }
                Type type = new TypeToken<DataResponse<ArrayList<ItemPrevious>>>() {}.getType();
                DataResponse<ArrayList<ItemPrevious>> obj = GsonSingleton.get().fromJson(response, type);
                int size = 0;
                if (obj.data != null && !obj.data.isEmpty()) {
                    size = obj.data.size();
                    mAdapterPrevious.addAll(obj.data);
                    mIndexPrevious = mAdapterPrevious.getLast().sid;
                }
                mAdapterPrevious.setHasMore(size >= BaseParam.DEFAULT_LIMIT);
                mAdapterPrevious.setLoaded();
            }
        });
    }

    private void loadBask() {
        DetailBaskParam param = new DetailBaskParam();
        param.gid = mData.gid;
        param.start = mIndexBask;
        BaseRequest<DetailBaskParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                if (mIndexBask == 0) {
                    mAdapterBask.clear();
                }
                Type type = new TypeToken<DataResponse<ArrayList<ItemBask>>>() {}.getType();
                DataResponse<ArrayList<ItemBask>> obj = GsonSingleton.get().fromJson(response, type);
                int size = 0;
                if (obj.data != null && !obj.data.isEmpty()) {
                    mAdapterBask.addAll(obj.data);
                    size = obj.data.size();
                    mIndexBask = mAdapterBask.getLast().bid;
                }
                mAdapterBask.setHasMore(size >= BaseParam.DEFAULT_LIMIT);
                mAdapterBask.setLoaded();
            }
        });
    }

    public void onClickMore(View view) {
        EventAgent.onEvent(R.string.goods_detail_record);
        startActivity(BidRecordActivity.newIntent(mData.sid, mData.status));
    }

    public void onClickDetail(View view) {
        EventAgent.onEvent(R.string.goods_detail_more);
        String title = getString(R.string.goods_detail);
        String url = "http://" + AppInstance.getInstance().getHost() + "/web/goods/detail?gid=" + mData.gid;
        startActivity(WebActivity.newIntent(title, url));
    }

    public void onClickRule(View view) {
        EventAgent.onEvent(R.string.goods_detail_rules);
        RuleDialog.newInstance().show(getSupportFragmentManager(), "rule");
    }

    public void onClickNotLogin(View view) {
        if (AppInstance.getInstance().isLogin()) {
            return;
        }

        startActivity(LoginActivity.newIntent(new LoginActivity.OnLoginListener() {
            @Override
            public void onLogin() {
                loadAuctionCoin();
            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.tvRules.requestLayout();
    }

    public void onClickChart(View view) {
        startActivity(ChartActivity.newIntent(mData.getItemGoods()));
    }
}
