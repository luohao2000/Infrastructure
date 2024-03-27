package com.itiaoling.metric.instrument;

/**
 * 是否可重置
 *
 * @author charles
 * @date 2023/10/17
 */
public interface Reset {
    /**
     * 重置计数器
     */
    void reset();
}
