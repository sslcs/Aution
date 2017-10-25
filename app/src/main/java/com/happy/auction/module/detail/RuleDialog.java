package com.happy.auction.module.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.databinding.FragmentRuleBinding;

/**
 * 规则对话框<br/>
 * Created by LiuCongshan on 17-10-24.
 *
 * @author LiuCongshan
 */
public class RuleDialog extends BottomSheetDialogFragment {
    private FragmentRuleBinding mBinding;

    public static RuleDialog newInstance() {
        return new RuleDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentRuleBinding.inflate(inflater);
        initLayout();
        return mBinding.getRoot();
    }

    private void initLayout() {
        mBinding.tvContent.setMovementMethod(new ScrollingMovementMethod());
        mBinding.setFragment(this);
    }

    public void onClickCancel(View view) {
        dismissAllowingStateLoss();
    }
}
