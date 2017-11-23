package com.happy.auction.module.detail;

import android.databinding.ObservableInt;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.happy.auction.R;
import com.happy.auction.databinding.DialogNumberPickerBinding;

/**
 * @author LiuCongshan
 * @date 17-11-22
 */

public class NumberPicker extends PopupWindow {
    private DialogNumberPickerBinding mBinding;
    private ObservableInt mNumber;
    private AuctionDetailActivity mActivity;

    public NumberPicker(AuctionDetailActivity activity, ObservableInt number) {
        mNumber = number;
        mActivity = activity;
        mBinding = DialogNumberPickerBinding.inflate(LayoutInflater.from(activity));
        setContentView(mBinding.getRoot());
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);

        mBinding.setPicker(this);
        mBinding.setNumber(number);
    }

    public void reset() {
        mBinding.tv0.setSelected(false);
        mBinding.tv1.setSelected(false);
        mBinding.tv2.setSelected(false);
        mBinding.tv3.setSelected(false);
    }

    public void onClickNumber(View view) {
        reset();
        view.setSelected(true);
        TextView tv = (TextView) view;
        String text = tv.getText().toString();
        int number = Integer.parseInt(text);
        mNumber.set(number);
    }

    public SpannableString formatSelectNumber(ObservableInt number) {
        String origin = mActivity.getString(R.string.selected_number, number.get());
        SpannableString ss = new SpannableString(origin);
        int color = mActivity.getColorCompat(R.color.main_red);
        ss.setSpan(new ForegroundColorSpan(color), 4, ss.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
