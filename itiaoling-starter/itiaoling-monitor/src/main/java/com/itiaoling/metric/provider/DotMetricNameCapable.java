package com.itiaoling.metric.provider;

import com.google.common.collect.Lists;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.Tag;

import java.util.List;
import java.util.Map;

/**
 * 带点的指标名称
 * prometheus 会自动转化为下划线格式
 *
 * @author gary fu
 */
public class DotMetricNameCapable {
    /**
     * 线程本地变量
     */
    private static final ThreadLocal<StringBuilder> STRING_BUILD_THREAD_LOCAL = ThreadLocal.withInitial(() ->
            new StringBuilder(64)
    );

    /**
     * 生成带点的指标名称
     *
     * @param name   指标名称
     * @param suffix 后缀
     * @return 带点的指标名称
     */
    protected String makeMetricSuffix(String name, String suffix) {
        if (name.endsWith(suffix)) {
            return name;
        }

        StringBuilder builder = STRING_BUILD_THREAD_LOCAL.get();
        builder.setLength(0);
        builder.append(name);
        builder.append('.').append(suffix);
        return builder.toString();
    }

    /**
     * 提取标签
     *
     * @param tags 标签
     */
    protected static List<Tag> extractedTags(Map<String, String> tags) {
        List<Tag> tagList = Lists.newArrayList();
        if (tags != null && tags.size() > 0) {
            tags.forEach((k, v) -> tagList.add(new ImmutableTag(k, v)));
        }
        return tagList;
    }
}
