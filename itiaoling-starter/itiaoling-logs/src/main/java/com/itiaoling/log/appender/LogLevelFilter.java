package com.itiaoling.log.appender;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import com.google.common.collect.ImmutableSet;

import java.util.Set;


/**
 * 日志级别过滤器
 *
 * @author gary fu
 */
public class LogLevelFilter extends Filter<ILoggingEvent> {

    private final Set<Level> levels = ImmutableSet.of(Level.INFO, Level.WARN, Level.ERROR, Level.DEBUG);

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }

        if (levels.contains(event.getLevel())) {
            return FilterReply.ACCEPT;
        } else {
            return FilterReply.DENY;
        }
    }
}