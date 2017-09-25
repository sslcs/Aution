package com.happy.auction.glide;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * Created by LiuCongshan on 17-9-25.
 * 根据view的宽高对图片进行等比缩放
 */

public class OriginalTransform extends BitmapTransformation {
    private int viewWidth, viewHeight;

    public OriginalTransform(int width, int height) {
        this.viewWidth = width;
        this.viewHeight = height;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap resource, int outWidth, int outHeight) {
        if (resource.getWidth() > viewWidth || resource.getHeight() > viewHeight) {
            float scaleWidth = resource.getWidth() / 1f / viewWidth;
            float scaleHeight = resource.getHeight() / 1f / viewHeight;
            int scaledWidth, scaledHeight;
            if (scaleWidth > scaleHeight) {
                scaledWidth = viewWidth;
                scaledHeight = (int) (resource.getHeight() / scaleWidth);
            } else {
                scaledWidth = (int) (resource.getWidth() / scaleHeight);
                scaledHeight = viewHeight;
            }
            resource = Bitmap.createScaledBitmap(resource, scaledWidth, scaledHeight, true);
        }
        return resource;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}
