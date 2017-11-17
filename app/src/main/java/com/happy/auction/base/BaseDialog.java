package com.happy.auction.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.happy.auction.R;

/**
 * 弹框基类
 *
 * @author LiuCongshan
 * @date 17-11-1
 */

public abstract class BaseDialog extends DialogFragment {

    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), R.style.BaseDialog);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (isAdded()) {
            return;
        }
        if (getParentFragment() != null) {
            if (getParentFragment().isHidden() || !getParentFragment().isVisible()) {
                return;
            }
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }
}
