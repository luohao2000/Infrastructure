package com.itiaoling.log.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 参数日志配置类
 *
 * @author charles
 * @date 2023/10/11
 * @see com.itiaoling.log.annotation.ParamClassLog
 */
@Data
@ConfigurationProperties(prefix = "itiaoling.logs.param-log", ignoreInvalidFields = true)
public class ParamLogProperties {

    /**
     * 是否启用脱敏
     */
    private boolean desensitizationEnabled = true;

    /**
     * 脱敏字段
     */
    private String[] desensitizationFields = new String[]{};
}
