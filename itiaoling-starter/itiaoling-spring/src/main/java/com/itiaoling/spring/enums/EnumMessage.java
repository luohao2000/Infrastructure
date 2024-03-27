package com.itiaoling.spring.enums;

/**
 * 枚举通用实现功能
 *
 * @author charles
 * @date 2023/12/4
 */
public interface EnumMessage {

    /**
     * 获取枚举编码
     *
     * @return String
     */
    Integer getCode();

    /**
     * 获取枚举信息
     *
     * @return String
     */
    String getMessage();
}
