package com.itiaoling.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 监控相关配置
 *
 * @author charles
 * @since 2023/12/15
 */
@ConfigurationProperties(prefix = "itiaoling.monitor", ignoreInvalidFields = true)
public class MonitorProperties {
    /**
     * 注入切面默认不注入
     */
    private Boolean enableAspect = false;

    public Boolean getEnableAspect() {
        return enableAspect;
    }

    public void setEnableAspect(Boolean enableAspect) {
        this.enableAspect = enableAspect;
    }
}
