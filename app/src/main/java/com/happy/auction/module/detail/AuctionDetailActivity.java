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
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
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
    private static final int REQUEST_CODE_LOGIN = 100;

    private final ObservableInt times = new ObservableInt(1);
    private ActivityAuctionDetailBinding binding;
    private AuctionDetailBidAdapter adapter;
    private AuctionDetail auctionDetail;
    private AuctionCoin auctionCoin;

    private int indexPrevious = 0;
    private int indexBask = 0;
    private PreviousAdapter adapterPrevious;
    private BaskAdapter adapterBask;

    public static Intent newIntent(BaseGoods goods) {
        Intent intent = new Intent(AppInstance.getInstance(), AuctionDetailActivity.class);
        intent.putExtra("goods", goods);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auction_detail);
        initLayout();
    }

    private void initLayout() {
        binding.tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        binding.tvMarketPrice.requestFocus();

        binding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                showBlackTitle(verticalOffset);
            }
        });

        adapter = new AuctionDetailBidAdapter();
        binding.listRecord.setAdapter(adapter);
        DecorationSpace decoration = new DecorationSpace();
        decoration.enableHeader();
        binding.listRecord.addItemDecoration(decoration);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.listRecord.setLayoutManager(manager);

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
        binding.vList.addItemDecoration(decoration);
        binding.vList.setAdapter(adapterPrevious);

        BaseGoods goods = (BaseGoods) getIntent().getSerializableExtra("goods");
        if (goods instanceof ItemGoods) {
            auctionDetail = new AuctionDetail((ItemGoods) goods);
        } else {
            auctionDetail = new AuctionDetail(goods);
        }

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    binding.vList.setAdapter(adapterBask);
                    if (adapterBask.isEmpty()) {
                        loadBask();
                    }
                } else {
                    binding.vList.setAdapter(adapterPrevious);
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
        int offset = -binding.tvAuctionStatus.getTop();
        if (verticalOffset > offset) {
            binding.tvToolbarPrice.setVisibility(View.GONE);
            binding.tvToolbarTime.setVisibility(View.GONE);
            return;
        }

        binding.tvToolbarPrice.setVisibility(View.VISIBLE);
        binding.tvToolbarTime.setVisibility(View.VISIBLE);
        int height = offset - verticalOffset;
        ConstraintLayout.LayoutParams paramsPrice = (ConstraintLayout.LayoutParams) binding.tvToolbarPrice.getLayoutParams();
        ConstraintLayout.LayoutParams paramsTime = (ConstraintLayout.LayoutParams) binding.tvToolbarTime.getLayoutParams();
        int heightTitle = binding.tvToolbarTitle.getHeight();
        if (height < heightTitle) {
            paramsPrice.height = height;
            paramsTime.height = height;
        } else {
            paramsPrice.height = heightTitle;
            paramsTime.height = heightTitle;
        }
        binding.tvToolbarPrice.setLayoutParams(paramsPrice);
        binding.tvToolbarTime.setLayoutParams(paramsTime);
    }

    private void initData() {
        binding.setActivity(this);
        binding.setBidTimes(times);
        binding.setData(auctionDetail);
        binding.tvAuctionStatus.setSyncView(binding.tvToolbarTime);
        if (auctionDetail.status == 0) {
            binding.tvAuctionStatus.finish();
        } else {
            binding.tvAuctionStatus.setExpireTime(auctionDetail.bid_expire_time);
            binding.tvAuctionStatus.setRepeat(auctionDetail.current_price == auctionDetail.bid_start_price);
        }

        if (auctionDetail.bid_records != null && !auctionDetail.bid_records.isEmpty()) {
            adapter.addAll(auctionDetail.bid_records);
            binding.setNewBid(auctionDetail.bid_records.get(0));
            setBtnMoreVisibility();
        }
    }

    private void setBtnMoreVisibility() {
        binding.tvMore.setVisibility(adapter.getRealCount() > 3 ? View.VISIBLE : View.GONE);
    }

    private void listenEvents() {
        RxBus.getDefault().subscribe(this, BidEvent.class, new Consumer<BidEvent>() {
            @Override
            public void accept(BidEvent event) throws Exception {
                if (event.sid != auctionDetail.sid) return;
                auctionDetail.setCurrentPrice(event.current_price);
                binding.tvAuctionStatus.setExpireTime(event.bid_expire_time);
                binding.tvAuctionStatus.setRepeat(false);

                BidRecord record = new BidRecord(event);
                if (TextUtils.isEmpty(record.uid)) return;
                adapter.addItem(record);
                binding.setNewBid(record);
                setBtnMoreVisibility();

                if (!record.uid.equals(AppInstance.getInstance().uid)) return;
                if (auctionCoin.current_bid_coins > 0) {
                    int progress = auctionCoin.current_bidden_coins + 1;
                    if (progress == auctionCoin.current_bid_coins) {
                        auctionCoin.setCurrentProgress(0);
                        auctionCoin.setCurrentCoin(0);
                    } else {
                        auctionCoin.setCurrentProgress(progress);
                    }
                }
            }
        });

        RxBus.getDefault().subscribe(this, AuctionEndEvent.class, new Consumer<AuctionEndEvent>() {
            @Override
            public void accept(AuctionEndEvent event) throws Exception {
                if (event.sid != auctionDetail.sid) return;
                auctionDetail.setStatus(0);
                binding.tvAuctionStatus.finish();
                loadAuctionCoin();
            }
        });
    }

    private void loadData() {
        AuctionDetailParam param = new AuctionDetailParam();
        param.sid = auctionDetail.sid;
        BaseRequest<AuctionDetailParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<AuctionDetail>>() {}.getType();
                DataResponse<AuctionDetail> obj = GsonSingleton.get().fromJson(response, type);
                auctionDetail = obj.data;
                initData();
                loadPrevious();
            }
        });
    }

    private void loadLatest() {
        AuctionDetailParam param = new AuctionDetailParam();
        param.gid = auctionDetail.gid;
        BaseRequest<AuctionDetailParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<AuctionDetail>>() {}.getType();
                DataResponse<AuctionDetail> obj = GsonSingleton.get().fromJson(response, type);
                auctionDetail = obj.data;
                initData();
                loadAuctionCoin();
            }
        });
    }

    private void loadAuctionCoin() {
        AuctionCoinParam param = new AuctionCoinParam();
        param.sid = auctionDetail.sid;
        BaseRequest<AuctionCoinParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<AuctionCoin>>() {}.getType();
                DataResponse<AuctionCoin> obj = GsonSingleton.get().fromJson(response, type);
                auctionCoin = obj.data;
                binding.setCoin(auctionCoin);
            }
        });
    }

    public void onClickBid(View view) {
        if (!AppInstance.getInstance().isLogin()) {
            Intent login = new Intent(this, LoginActivity.class);
            startActivityForResult(login, REQUEST_CODE_LOGIN);
            return;
        }

        if (times.get() == 0) times.set(1);

        getBalance();
    }

    private void bid() {
        final BidParam param = new BidParam();
        param.sid = auctionDetail.sid;
        param.buy = times.get();
        param.take_coin = param.buy;
        BaseRequest<BidParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                if (AppInstance.getInstance().getUser().free_coin >= param.buy) {
                    auctionCoin.setBidGiftCoin(auctionCoin.bid_gift_coin + param.buy);
                    if (param.buy > 1) {
                        auctionCoin.setCurrentProgress(0);
                        auctionCoin.setCurrentCoin(param.buy);
                    }
                } else {
                    loadAuctionCoin();
                }
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                ToastUtil.show(message);
            }
        });
    }


    public void onClickNext(View view) {
        adapter.clear();
        binding.setCoin(null);
        binding.setNewBid(null);
        setBtnMoreVisibility();
        loadLatest();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (auctionDetail.status != 0) {
            binding.tvAuctionStatus.cancel();
        }
        RxBus.getDefault().unsubscribe(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_CODE_LOGIN) {
            onClickBid(null);
        }
    }

    public void onClickMinus(View view) {
        if (times.get() > 1) {
            times.set(times.get() - 1);
        }
    }

    public void onClickPlus(View view) {
        times.set(times.get() + 1);
    }

    public void afterTextChanged(Editable editable) {
        try {
            int number = Integer.parseInt(editable.toString());
            times.set(number);
        } catch (NumberFormatException e) {
            times.set(0);
        }
    }

    public void onClickCancel(View view) {
        AuctionCancelParam param = new AuctionCancelParam();
        param.sid = auctionDetail.sid;
        BaseRequest<AuctionCancelParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                auctionCoin.setCurrentCoin(0);
                loadAuctionCoin();
            }
        });
    }

    public void onClickAutoBid(View view) {}

    private void loadPrevious() {
        PreviousParam param = new PreviousParam();
        param.gid = auctionDetail.gid;
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
        param.gid = auctionDetail.gid;
        param.start = indexBask;
        BaseRequest<DetailBaskParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                adapterBask.setLoaded();
                Type type = new TypeToken<DataResponse<ArrayList<ItemBask>>>() {}.getType();
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
        startActivity(BidRecordActivity.newIntent(auctionDetail.sid, auctionDetail.status));
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
