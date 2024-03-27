package com.itiaoling.metric.monitor;

import com.itiaoling.metric.ExceptionUtils;
import com.itiaoling.metric.constants.TagName;
import com.itiaoling.metric.constants.TagValue;
import com.itiaoling.metric.spec.Monitor;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author charles
 * @since 2024/1/10
 */
public class MonitorExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(MonitorExecutor.class);

    private final Monitor taggedMonitor;

    private final String productLine;

    private final String dataCenter;

    public MonitorExecutor(Monitor taggedMonitor, String productLine, String dataCenter) {
        this.taggedMonitor = taggedMonitor;
        this.productLine = productLine;
        this.dataCenter = dataCenter;
    }

    /**
     * 记录
     *
     * @param joinPoint    方法签名
     * @param tagList      标签
     * @param monitorEvent 监控事件
     * @param <T>          返回类型
     * @return 返回执行结果
     * @throws Throwable 执行过程中的异常
     */
    public <T> T recordJoinPoint(String joinPoint, List<Tag> tagList, MonitorEvent<T> monitorEvent) throws Throwable {
        T result = null;
        Throwable e = null;

        long start = System.currentTimeMillis();
        try {
            result = monitorEvent.runWithMonitor();
        } catch (Throwable exception) {
            e = exception;
        }
        long duration = System.currentTimeMillis() - start;

        boolean success = e == null;
        recordJoinPoint(joinPoint, success, success ? null : e.getClass().getSimpleName(), duration, tagList);
        if (!success) {
            throw e;
        }
        return result;
    }


    /**
     * 切点记录
     *
     * @param joinPoint       方法签名
     * @param success         是否成功
     * @param caughtException 异常
     * @param duration        耗时
     */
    public void recordJoinPoint(String joinPoint, boolean success, String caughtException, long duration, List<Tag> extraTags) {
        try {
            // 基础标签
            List<Tag> tagList = new ArrayList<>();
            tagList.add(new ImmutableTag(TagName.SOURCE, Optional.ofNullable(joinPoint).orElse("unknownJoinPoint")));
            tagList.add(new ImmutableTag(TagName.RESULT, success ? TagValue.RESULT_SUCCESS : TagValue.RESULT_FAIL));
            tagList.add(new ImmutableTag(TagName.EXCEPTION, Optional.ofNullable(caughtException).orElse(TagValue.RESULT_EXCEPTION_NONE)));
            tagList.add(new ImmutableTag(TagName.PRODUCT_LINE, Optional.ofNullable(productLine).orElse("unknownPipeline")));
            tagList.add(new ImmutableTag(TagName.DATA_CENTER, Optional.ofNullable(dataCenter).orElse("unknownDataCenter")));

            if (extraTags != null && !extraTags.isEmpty()) {
                tagList.addAll(extraTags);
            }

            taggedMonitor.record(tagList, duration, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            LOG.info("monitor record error:", e);
        }
    }

    public void recordResponse(String httpMethod, String url, String statusCode, String payload, long duration, Throwable e) {
        try {
            List<Tag> tagList = new ArrayList<>();
            tagList.add(new ImmutableTag(TagName.SOURCE, TagValue.SOURCE_WEB_FILTER));
            tagList.add(new ImmutableTag(TagName.HTTP_METHOD, httpMethod));
            tagList.add(new ImmutableTag(TagName.HTTP_STATUS, statusCode));
            tagList.add(new ImmutableTag(TagName.URL, url));
            if (e == null) {
                tagList.add(new ImmutableTag(TagName.BIZ_CODE, ExceptionUtils.getResultCode(payload)));
            } else {
                tagList.add(new ImmutableTag(TagName.BIZ_CODE, ExceptionUtils.getExceptionCode(e)));
            }

            tagList.add(new ImmutableTag(TagName.PRODUCT_LINE, productLine));
            tagList.add(new ImmutableTag(TagName.DATA_CENTER, dataCenter));
            taggedMonitor.record(tagList, duration, TimeUnit.MILLISECONDS);
        } catch (Exception ne) {
            LOG.warn("monitorResponse error", ne);
        }
    }
}
