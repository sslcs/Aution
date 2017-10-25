package com.happy.auction.entity.param;

/**
 * 获取配置信息参数
 *
 * @author LiuCongshan
 * @date 17-8-21
 */

public class ConfigParam extends BaseParam {
    /**
     * invitation 邀请, protocol 服务协议， service 客服中心
     */
    public String alias = "invitation";

    public ConfigParam() {
        action = "config_get";
    }
}
