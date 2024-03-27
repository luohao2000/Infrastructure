package com.itiaoling.spring.response;

/**
 * 统一返回结果
 *
 * @param <T>
 * @author charles
 */
public class RestResult<T> {
    /**
     * 请求id
     */
    private String requestId;
    /**
     * 主响应码
     */
    private Integer code;
    /**
     * 主响应消息
     */
    private String message;
    /**
     * 主响应数据
     */
    private T result;
    /**
     * 调用是否成功 true：是 false：否
     */
    private Boolean success;


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
