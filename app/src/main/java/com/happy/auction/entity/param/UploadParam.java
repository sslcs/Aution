package com.happy.auction.entity.param;

import com.google.gson.annotations.SerializedName;

/**
 * 获取上传图片参数<br/>
 * Created by LiuCongshan on 17-10-25.
 */

public class UploadParam extends BaseParam {
    public static final String TYPE_AVATAR = "avatar";
    public static final String TYPE_BASK = "bask";

    /**
     * 需要的token数量, 不传则为一
     */
    public int amount = 1;
    /**
     * {@link #TYPE_AVATAR}: 用户头像， {@link #TYPE_AVATAR}: 晒单
     */
    @SerializedName("upload_type")
    public String type;

    public UploadParam() {
        action = "cdn_get_token";
    }
}
