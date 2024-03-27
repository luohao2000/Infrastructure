package com.itiaoling.spring.exception;


import com.itiaoling.spring.enums.CustomError;
import com.itiaoling.spring.enums.EnumMessage;

/**
 * 通用的异常类
 *
 * @author charles
 */
public class CustomException extends RuntimeException implements IException {
    /**
     * 错误码
     */
    private final int code;
    /**
     * 错误信息
     */
    private final String msg;

    public CustomException(int code) {
        this(code, CustomError.SYS_ERROR.getMessage());
    }

    public CustomException(String errorMsg) {
        super(errorMsg);
        this.code = CustomError.SYS_ERROR.getCode();
        this.msg = errorMsg;
    }

    public CustomException(int code, String errorMsg) {
        super(errorMsg);
        this.code = code;
        this.msg = errorMsg;
    }

    public CustomException(EnumMessage enumMessage) {
        super(enumMessage.getMessage());
        this.code = enumMessage.getCode();
        this.msg = enumMessage.getMessage();
    }


    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public Integer getErrorCode() {
        return code;
    }

    @Override
    public String getErrorMsg() {
        return msg;
    }
}
