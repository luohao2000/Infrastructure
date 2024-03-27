package com.itiaoling.log.aop.param.template;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.filter.ValueFilter;
import com.google.common.collect.Sets;
import com.itiaoling.log.annotation.ParamMethodLog;
import com.itiaoling.log.properties.ParamLogProperties;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author charles
 * @date 2023/10/12
 */
public abstract class LogParamAbstractTemplate implements ParamLogTemplate {

    protected ParamLogProperties paramLogProperties;

    protected final static String DEFAULT_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    protected LogParamAbstractTemplate(ParamLogProperties paramLogProperties) {
        this.paramLogProperties = paramLogProperties;
    }

    protected String toJsonWithDesensitization(Object object, ParamMethodLog paramMethodLog) {
        final Set<String> configDesensitizationFields = Sets.newHashSet(paramLogProperties.getDesensitizationFields());
        if (paramMethodLog != null) {
            Set<String> annotationDesensitizationFields = Sets.newHashSet(paramMethodLog.desensitizedFields());
            if (!CollectionUtils.isEmpty(annotationDesensitizationFields)) {
                configDesensitizationFields.addAll(annotationDesensitizationFields);
            }
        }

        if (CollectionUtils.isEmpty(configDesensitizationFields)) {
            return JSON.toJSONString(object);
        }

        ValueFilter valueFilter = (obj, name, value) -> {
            for (String configDesensitizationField : configDesensitizationFields) {
                if (configDesensitizationField.equalsIgnoreCase(name)) {
                    return "******";
                }
            }
            return value;
        };
        if (object instanceof Object[]) {
            Object[] objectArray = (Object[]) object;
            return Arrays.stream(objectArray).map(item -> JSON.toJSONString(item, valueFilter))
                    .collect(Collectors.joining(","));
        }

        return JSON.toJSONString(object, valueFilter);
    }

    protected String formatTimeWithDefault(long time) {
        return new SimpleDateFormat(DEFAULT_TIME_FORMAT_PATTERN).format(time);
    }
}
