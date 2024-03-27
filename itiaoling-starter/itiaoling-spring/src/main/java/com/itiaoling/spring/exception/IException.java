package com.itiaoling.spring.exception;


/**
 * @author charles
 */
public interface IException {
    /**
     * 获取错误码
     *
     * @return Integer
     */
    Integer getErrorCode();

    /**
     * 获取错误信息
     *
     * @return String
     */
    String getErrorMsg();
}
