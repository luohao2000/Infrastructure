package com.itiaoling.metric.monitor;

import com.itiaoling.metric.constants.TagName;
import com.itiaoling.metric.spec.Monitor;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * 编程式命名监控器
 *
 * @author gary fu
 */
abstract class AbstractMonitor implements Monitor {

    /**
     * 重置应用名称
     *
     * @param applicationName 应用名称
     * @param tagList         tag 列表
     * @return tag 列表
     */
    protected List<Tag> resetApplicationName(String applicationName, List<Tag> tagList) {
        List<Tag> finalTagList = new ArrayList<>();
        if (tagList == null || tagList.isEmpty()) {
            finalTagList.add(new ImmutableTag(TagName.APPLICATION_NAME, applicationName));
            return finalTagList;
        }

        tagList.removeIf(tag -> TagName.APPLICATION_NAME.equals(tag.getKey()));

        tagList.add(new ImmutableTag(TagName.APPLICATION_NAME, applicationName));
        return tagList;
    }
}
