package com.itiaoling.spring.response;

import com.itiaoling.spring.enums.EnumMessage;

/**
 * 返回对象工厂
 *
 * @author charles
 */
public class RestResultFactory {

    public static final int HTTP_STATUS_OK = 200;

    /**
     * 统一返回正确处理结果
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return restful result
     */
    public static <T> RestResult<T> success(T data) {
        RestResult<T> restResult = new RestResult<>();
        restResult.setCode(HTTP_STATUS_OK);
        restResult.setResult(data);
        restResult.setSuccess(true);
        restResult.setMessage(null);
        return restResult;
    }

    /**
     * @param <T> 数据类型
     * @return restful result
     */
    public static <T> RestResult<T> success() {
        return success(null);
    }

    public static <T> RestResult<T> error(Integer errCode, String errMsg) {
        RestResult<T> restResult = new RestResult<>();
        restResult.setCode(errCode);
        restResult.setMessage(errMsg);
        restResult.setResult(null);
        restResult.setSuccess(false);
        return restResult;
    }

    /**
     * 通用结果返回
     *
     * @param enumMessage 错误码
     * @return restResult
     */
    public static <T> RestResult<T> error(EnumMessage enumMessage) {
        RestResult<T> restResult = new RestResult<>();
        restResult.setCode(enumMessage.getCode());
        restResult.setMessage(enumMessage.getMessage());
        restResult.setResult(null);
        restResult.setSuccess(false);
        return restResult;
    }
}
