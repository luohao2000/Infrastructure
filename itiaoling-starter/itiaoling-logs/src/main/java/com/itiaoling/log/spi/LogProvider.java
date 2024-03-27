package com.itiaoling.log.spi;

import java.util.Map;
import java.util.concurrent.CountDownLatch;


/**
 * 日志输出提供者
 *
 * @author gary fu
 */
public interface LogProvider {

    /**
     * 获取其倒计时信号灯
     *
     * @return 信号灯
     */
    default CountDownLatch getCountDownLatch(){
        return null;
    }


    /**
     * 输出日志
     *
     * @param scenario     场景
     * @param msg          具体的日志消息
     * @param customerTags 自定义日志标签，单条生命周期
     * @param storedTags   自定义日志标签，单会话生命周期
     */
    void log(String scenario,
             String msg,
             Map<String, String> customerTags,
             Map<String, String> storedTags);
}
