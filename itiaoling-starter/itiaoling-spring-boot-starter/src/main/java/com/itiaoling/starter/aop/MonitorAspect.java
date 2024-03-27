package com.itiaoling.starter.aop;

import com.itiaoling.metric.annotation.MeterTag;
import com.itiaoling.metric.annotation.MetricRecord;
import com.itiaoling.metric.constants.TagName;
import com.itiaoling.metric.constants.TagValue;
import com.itiaoling.metric.monitor.MonitorExecutor;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.Tag;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * @author charles
 * @since 2024/1/9
 */
@Aspect
public class MonitorAspect {

    private static final Logger LOG = LoggerFactory.getLogger(MonitorAspect.class);

    @Resource
    MonitorExecutor monitorExecutor;

    @Around("@annotation(metricRecord)")
    public Object interceptAndRecord(ProceedingJoinPoint pjp, MetricRecord metricRecord) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        String methodName = method.getName();
        Class<?> declaringClass = method.getDeclaringClass();
        String packageName = declaringClass.getPackage().getName();
        String className = declaringClass.getSimpleName();
        String joinPoint = String.join(packageName, className, methodName, ".");

        MeterTag[] meterTags = metricRecord.extraTags();
        List<Tag> extraTagList = Arrays.stream(meterTags).map(tag -> new ImmutableTag(tag.key(), tag.value())).collect(Collectors.toList());

        // 异步处理
        boolean stopWhenCompleted = CompletionStage.class.isAssignableFrom(method.getReturnType());
        if (stopWhenCompleted) {
            extraTagList.add(new ImmutableTag(TagName.SYNCHRONIZATION, String.valueOf(false)));

            long start = System.currentTimeMillis();
            try {
                CompletionStage<?> completionStage = (CompletionStage<?>) pjp.proceed();
                return completionStage.whenComplete((result, throwable) -> {
                    long duration = System.currentTimeMillis() - start;
                    if (throwable != null) {
                        String exceptionTagValue = throwable.getCause() == null ? throwable.getClass().getSimpleName() : throwable.getCause().getClass().getSimpleName();
                        monitorExecutor.recordJoinPoint(joinPoint, false, exceptionTagValue, duration, extraTagList);
                        return;
                    }
                    monitorExecutor.recordJoinPoint(joinPoint, true, TagValue.RESULT_EXCEPTION_NONE, duration, extraTagList);
                });
            } catch (Throwable exception) {
                monitorExecutor.recordJoinPoint(joinPoint, false, exception.getClass().getSimpleName(), System.currentTimeMillis() - start, extraTagList);
                throw exception;
            }
        }

        // 同步处理
        extraTagList.add(new ImmutableTag(TagName.SYNCHRONIZATION, String.valueOf(true)));
        return monitorExecutor.recordJoinPoint(joinPoint, extraTagList, pjp::proceed);
    }
}
