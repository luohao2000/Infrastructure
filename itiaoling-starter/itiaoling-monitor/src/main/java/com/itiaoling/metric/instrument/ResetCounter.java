package com.itiaoling.metric.instrument;


import io.micrometer.core.instrument.Counter;

/**
 * 可重置计数器
 *
 * @author gary fu
 */
public interface ResetCounter extends Counter, Reset {


}
