package com.happy.auction.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * 弹框基类
 *
 * @author LiuCongshan
 * @date 17-11-1
 */

public abstract class BaseDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, 0);
        return super.onCreateDialog(savedInstanceState);
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
