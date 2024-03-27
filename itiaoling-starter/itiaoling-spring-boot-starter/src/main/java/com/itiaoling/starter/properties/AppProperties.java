package com.itiaoling.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * itiaoling 应用配置
 *
 * @author charles
 * @since 2023/12/15
 */
@EnableConfigurationProperties({MessageProperties.class, MonitorProperties.class})
@ConfigurationProperties(prefix = "itiaoling", ignoreInvalidFields = true)
public class AppProperties {
    /**
     * 全局异常处理 默认开启
     */
    private Boolean globalException = true;

    /**
     * 业务线
     */
    private String productLine;

    /**
     * 数据中心
     */
    private String dataCenter;

    /**
     * 消息相关配置
     */
    private MessageProperties message;

    public MessageProperties getMessage() {
        return message;
    }

    public void setMessage(MessageProperties message) {
        this.message = message;
    }

    public Boolean getGlobalException() {
        return globalException;
    }

    public void setGlobalException(Boolean globalException) {
        this.globalException = globalException;
    }

    public String getDataCenter() {
        return dataCenter;
    }

    public String getProductLine() {
        return productLine;
    }

    public void setDataCenter(String dataCenter) {
        this.dataCenter = dataCenter;
    }

    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }
}
