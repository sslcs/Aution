package com.happy.auction.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.R;
import com.happy.auction.databinding.FragmentDialogBuilderBinding;

/**
 * 自定义对话框
 * Created by LiuCongshan on 17-8-14.
 */
public class CustomDialog extends DialogFragment {
    private static final String EXTRA_TITLE = "extra_title";
    private static final String EXTRA_CONTENT = "extra_content";
    private static final String EXTRA_LEFT_TEXT = "extra_left_text";
    private static final String EXTRA_RIGHT_TEXT = "extra_right_text";
    private static final String EXTRA_GRAVITY = "extra_gravity";
    private static final String EXTRA_LEFT_TEXT_COLOR = "extra_left_text_color";
    private static final String EXTRA_RIGHT_TEXT_COLOR = "extra_right_text_color";
    private static final String EXTRA_RIGHT_BACKGROUND = "extra_right_background";

    protected String mTitle;
    protected String mContent;
    protected String mTextLeft;
    protected String mTextRight;

    private OnClickListener mLeftListener;
    private OnClickListener mRightListener;

    private int mGravity;
    private int textColorRight;
    private int textColorLeft;
    private int rightTextBgColor;

    private FragmentDialogBuilderBinding binging;

    public void setLeftOnClickListener(OnClickListener listener) {
        mLeftListener = listener;
    }

    public void setRightOnClickListener(OnClickListener listener) {
        mRightListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), R.style.TransparentDialog);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(EXTRA_TITLE);
            mContent = getArguments().getString(EXTRA_CONTENT);
            mTextLeft = getArguments().getString(EXTRA_LEFT_TEXT);
            mTextRight = getArguments().getString(EXTRA_RIGHT_TEXT);
            mGravity = getArguments().getInt(EXTRA_GRAVITY, Gravity.CENTER);
            textColorLeft = getArguments().getInt(EXTRA_LEFT_TEXT_COLOR, 0);
            textColorRight = getArguments().getInt(EXTRA_RIGHT_TEXT_COLOR, 0);
            rightTextBgColor = getArguments().getInt(EXTRA_RIGHT_BACKGROUND, 0);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binging.tvContent.setGravity(mGravity);
        if (!TextUtils.isEmpty(mContent)) {
            if (mContent.contains("<br/>") || mContent.contains("</font>")) {
                binging.tvContent.setText(Html.fromHtml(mContent));
            } else {
                binging.tvContent.setText(mContent);
            }
        }

        if (!TextUtils.isEmpty(mTitle)) {
            binging.tvTitle.setVisibility(View.VISIBLE);
            binging.tvTitle.setText(mTitle);
        }

        if (!TextUtils.isEmpty(mTextLeft)) {
            binging.btnLeft.setText(mTextLeft);
            binging.btnLeft.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(mTextRight)) {
                binging.divider.setVisibility(View.GONE);
            }
        }

        if (!TextUtils.isEmpty(mTextRight)) {
            binging.btnRight.setText(mTextRight);
            binging.btnRight.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(mTextLeft)) {
                binging.divider.setVisibility(View.GONE);
            }
        }

        binging.btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLeftListener == null) {
                    dismissAllowingStateLoss();
                    return;
                }
                mLeftListener.onClick(CustomDialog.this);
            }
        });
        binging.btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRightListener == null) {
                    dismissAllowingStateLoss();
                    return;
                }
                mRightListener.onClick(CustomDialog.this);
            }
        });

        if (textColorRight > 0) {
            binging.btnRight.setTextColor(getResources().getColor(textColorRight));
        }
        if (textColorLeft > 0) {
            binging.btnLeft.setTextColor(getResources().getColor(textColorLeft));
        }
        if (rightTextBgColor > 0) {
            binging.divider.setVisibility(View.GONE);
            binging.btnRight.setBackgroundColor(getResources().getColor(rightTextBgColor));
        }
    }

    public void onClickLeft(View view) {
        if (mLeftListener == null) {
            dismissAllowingStateLoss();
            return;
        }
        mLeftListener.onClick(CustomDialog.this);
    }

    public void onClickRight(View view) {
        if (mRightListener == null) {
            dismissAllowingStateLoss();
            return;
        }
        mRightListener.onClick(CustomDialog.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binging = FragmentDialogBuilderBinding.inflate(inflater);
        return binging.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContent = null;
        mTextLeft = null;
        mTextRight = null;
        mLeftListener = null;
        mRightListener = null;
    }

    public void show(FragmentManager manager, String tag) {
        if (isAdded()) return;
        if (getParentFragment() != null && (getParentFragment().isHidden()
                || !getParentFragment().isVisible())) return;
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    public interface OnClickListener {
        void onClick(DialogFragment dialog);
    }

    public static class Builder {
        private Bundle bundle;
        private OnClickListener mListenerLeft;
        private OnClickListener mListenerRight;

        public Builder() {
            bundle = new Bundle();
        }

        public Builder content(String content) {
            bundle.putString(EXTRA_CONTENT, content);
            return this;
        }

        public Builder title(String title) {
            bundle.putString(EXTRA_TITLE, title);
            return this;
        }

        public Builder textLeft(String text) {
            bundle.putString(EXTRA_LEFT_TEXT, text);
            return this;
        }

        public Builder textRight(String text) {
            bundle.putString(EXTRA_RIGHT_TEXT, text);
            return this;
        }

        public Builder textColorLeft(int color) {
            bundle.putInt(EXTRA_LEFT_TEXT_COLOR, color);
            return this;
        }

        public Builder textColorRight(int color) {
            bundle.putInt(EXTRA_RIGHT_TEXT_COLOR, color);
            return this;
        }

        public Builder backgroundRight(@ColorRes int color) {
            bundle.putInt(EXTRA_RIGHT_BACKGROUND, color);
            return this;
        }

        public Builder gravity(int gravity) {
            bundle.putInt(EXTRA_GRAVITY, gravity);
            return this;
        }

        public Builder setOnClickLeftListener(OnClickListener l) {
            mListenerLeft = l;
            return this;
        }

        public Builder setOnClickRightListener(OnClickListener l) {
            mListenerRight = l;
            return this;
        }

        public CustomDialog build() {
            CustomDialog dialog = new CustomDialog();
            dialog.setArguments(bundle);
            if (mListenerLeft != null) {
                dialog.setLeftOnClickListener(mListenerLeft);
            }
            if (mListenerRight != null) {
                dialog.setRightOnClickListener(mListenerRight);
            }
            return dialog;
        }

        public CustomDialog show(FragmentManager manager, String tag) {
            CustomDialog dialog = build();
            dialog.show(manager, tag);
            return dialog;
        }
    }
}
