package com.itiaoling.spring.enums;

/**
 * @author charles
 * @date 2023/12/4
 */
public enum CustomError implements EnumMessage {
    /**
     * 系统异常
     */
    SYS_ERROR(500, "系统异常"),
    ;

    private final Integer code;
    private final String message;

    CustomError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
