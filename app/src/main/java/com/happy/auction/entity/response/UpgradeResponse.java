package com.happy.auction.entity.response;

import com.google.gson.annotations.SerializedName;

/**
 * 请求升级信息返回结果
 *
 * @author LiuCongshan
 * @date 17-11-15
 */

public class UpgradeResponse {
    /**
     * App版本，string类型，用于显示
     */
    public String version;
    /**
     * App版本，int类型，用于跟当前版本比较，判断是否有新版本
     */
    public int version_number;
    /**
     * 更新说明
     */
    @SerializedName("changelog")
    public String change_log;
    /**
     * 下载链接
     */
    public String download_url;
    /**
     * 更新方式，0：不提示，1：提示用户，2：强制更新
     */
    public int update;
    /**
     * 发布时间
     */
    public long release_time;
}
