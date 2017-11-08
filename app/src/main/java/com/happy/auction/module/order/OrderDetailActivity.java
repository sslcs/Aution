package com.happy.auction.module.order;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.base.BaseBackActivity;
import com.happy.auction.databinding.ActivityOrderDetailBinding;
import com.happy.auction.entity.item.Address;
import com.happy.auction.entity.item.Contact;
import com.happy.auction.entity.item.ItemOrder;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.ConfirmAddressParam;
import com.happy.auction.entity.param.ContactParam;
import com.happy.auction.entity.param.OrderDetailParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.OrderDetail;
import com.happy.auction.module.address.AddressSelectActivity;
import com.happy.auction.module.address.ContactEditActivity;
import com.happy.auction.module.address.ContactSelectActivity;
import com.happy.auction.module.detail.AuctionDetailActivity;
import com.happy.auction.module.me.BaskMyActivity;
import com.happy.auction.module.me.CardActivity;
import com.happy.auction.module.pay.OrderPayActivity;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.EventAgent;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.StringUtil;
import com.happy.auction.utils.ToastUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 订单详情页面
 *
 * @author LiuCongshan
 */
public class OrderDetailActivity extends BaseBackActivity {
    private static final String KEY_ITEM = "KEY_ITEM";
    private static final int REQUEST_CODE_PAY = 100;
    private static final int REQUEST_CODE_CONTACT = 101;
    private static final int REQUEST_CODE_ADDRESS = 102;
    private static final int REQUEST_CODE_BASK = 103;

    private ActivityOrderDetailBinding mBinding;
    private OrderDetail mData;
    private ItemOrder mOrder;
    private boolean hasContact = false;

    public static Intent newIntent(ItemOrder item) {
        Intent intent = new Intent(AppInstance.getInstance(), OrderDetailActivity.class);
        intent.putExtra(KEY_ITEM, item);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        }
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);
        initLayout();
    }

    private void initLayout() {
        mBinding.refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        mOrder = (ItemOrder) getIntent().getSerializableExtra(KEY_ITEM);
        mData = new OrderDetail(mOrder);
        setData();

        loadData();
    }

    private void setData() {
        mBinding.setData(mData);
        setProgress();
        setBottom();
    }

    private void setProgress() {
        mBinding.progress0.setSelected(mData.status >= 2);
        mBinding.progress1.setSelected(mData.status >= 3);
        mBinding.progress2.setSelected(mData.status >= 4);
        mBinding.progress3.setSelected(mData.status >= 5);
        mBinding.tvProgress0.setSelected(mData.status == 2);
        mBinding.tvProgress1.setSelected(mData.status == 3);
        mBinding.tvProgress2.setSelected(mData.status == 4);
        mBinding.tvProgress3.setSelected(mData.status >= 5);
        mBinding.tvTimeProgress0.setText(StringUtil.formatTimeMinute(mData.prize_time));
        if (mData.pay_time != 0) {
            mBinding.tvTimeProgress1.setText(StringUtil.formatTimeMinute(mData.pay_time));
        }
        if (mData.confirm_prize_time != 0) {
            mBinding.tvTimeProgress2.setText(StringUtil.formatTimeMinute(mData.confirm_prize_time));
        }
        if (mData.bask_time != 0) {
            mBinding.tvTimeProgress3.setText(StringUtil.formatTimeMinute(mData.bask_time));
        }
    }

    private void loadData() {
        OrderDetailParam param = new OrderDetailParam();
        param.pid = mData.pid;
        BaseRequest<OrderDetailParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                mBinding.refreshView.setRefreshing(false);
                Type type = new TypeToken<DataResponse<OrderDetail>>() {}.getType();
                DataResponse<OrderDetail> obj = GsonSingleton.get().fromJson(response, type);
                if (obj.data != null) {
                    mData = obj.data;
                    setData();
                }
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                mBinding.refreshView.setRefreshing(false);
            }
        });
    }

    private void setBottom() {
        if (mData.status == OrderDetail.STATUS_CONFIRM) {
            // 已确认
            mBinding.btnBottom.setText(R.string.go_bask);
        } else if (mData.status >= OrderDetail.STATUS_BASK) {
            // 已晒单
            mBinding.btnBottom.setText(R.string.check_bask);
        } else if (mData.status == OrderDetail.STATUS_WIN) {
            // 已拍中
            mBinding.btnBottom.setText(R.string.go_pay);
        } else if (mData.type == 1) {
            // 已付款-实物
            mBinding.btnBottom.setText(R.string.confirm_address);
            loadContact();
        } else {
            // 已付款-虚拟物品
            mBinding.btnBottom.setText(R.string.select_virtual_address);
            loadContact();
        }
    }

    public void onClickBtnBottom(View view) {
        if (mData.status == OrderDetail.STATUS_CONFIRM) {
            // 已确认
            EventAgent.onEvent(R.string.reward_detail_share);
            startActivityForResult(BaskActivity.newIntent(mOrder.sid), REQUEST_CODE_BASK);
        } else if (mData.status >= OrderDetail.STATUS_BASK) {
            // 已晒单
            EventAgent.onEvent(R.string.reward_detail_shareorder);
            startActivity(BaskMyActivity.newIntent());
        } else if (mData.status == OrderDetail.STATUS_WIN) {
            // 已拍中
            EventAgent.onEvent(R.string.reward_detail_pay);
            Intent intent = OrderPayActivity.newIntent(mOrder);
            startActivityForResult(intent, REQUEST_CODE_PAY);
        } else if (mData.type == 1) {
            // 已付款-实物
            if (mData.address != null) {
                EventAgent.onEvent(R.string.reward_detail_address_confirm);
                onSelectAddress(mData.address.aid);
            } else {
                onClickChangeAddress(null);
            }
        } else {
            // 已付款-虚拟物品
            EventAgent.onEvent(R.string.reward_detail_number_option);
            Intent intent;
            if (hasContact) {
                intent = ContactSelectActivity.newIntent();
            } else {
                intent = ContactEditActivity.newIntent();
            }
            startActivityForResult(intent, REQUEST_CODE_CONTACT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (REQUEST_CODE_CONTACT == requestCode) {
            int id = -1;
            if (data.hasExtra(ContactSelectActivity.EXTRA_CONTACT_ID)) {
                id = data.getIntExtra(ContactSelectActivity.EXTRA_CONTACT_ID, -1);
            }
            if (id != -1) {
                onSelectAddress(id);
            } else {
                hasContact = false;
            }
        } else if (REQUEST_CODE_ADDRESS == requestCode) {
            mData.address = (Address) data.getSerializableExtra(AddressSelectActivity.EXTRA_ADDRESS);
            mBinding.setData(mData);
        } else if (REQUEST_CODE_BASK == requestCode) {
            mData.status = 5;
            mOrder.status = 5;
            setData();
        } else if (REQUEST_CODE_PAY == requestCode) {
            loadData();
        }
    }

    private void onSelectAddress(int id) {
        ConfirmAddressParam param = new ConfirmAddressParam();
        param.aid = id;
        param.sid = mData.sid;
        BaseRequest<ConfirmAddressParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                loadData();
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                ToastUtil.show(message);
            }
        });
    }

    public void onClickChangeAddress(View view) {
        EventAgent.onEvent(R.string.reward_detail_address_option);
        Intent intent = AddressSelectActivity.newIntent(mData.address.aid);
        startActivityForResult(intent, REQUEST_CODE_ADDRESS);
    }

    public void onClickCardPassword(View view) {
        EventAgent.onEvent(R.string.reward_detail_cardcord);
        startActivity(CardActivity.newIntent());
    }

    public void onClickDetail(View view) {
        EventAgent.onEvent(R.string.reward_detail_aution);
        startActivity(AuctionDetailActivity.newIntent(mOrder));
    }

    private void loadContact() {
        ContactParam param = new ContactParam();
        BaseRequest<ContactParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<ArrayList<Contact>>>() {}.getType();
                DataResponse<ArrayList<Contact>> obj = GsonSingleton.get().fromJson(response, type);
                if (obj.data != null && !obj.data.isEmpty()) {
                    hasContact = true;
                }
            }
        });
    }
}
