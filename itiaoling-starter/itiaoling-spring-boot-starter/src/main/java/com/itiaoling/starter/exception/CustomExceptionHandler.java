package com.itiaoling.starter.exception;

import com.itiaoling.spring.enums.CustomError;
import com.itiaoling.spring.exception.CustomException;
import com.itiaoling.spring.response.RestResult;
import com.itiaoling.spring.response.RestResultFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 通用全局异常捕获处理
 *
 * @author : charles
 */
@RestControllerAdvice
@ConditionalOnProperty(prefix = "itiaoling", name = "global-exception", havingValue = "true", matchIfMissing = true)
public class CustomExceptionHandler implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);
    public static final String ACTUATOR_URI = "actuator";

    @ExceptionHandler({Exception.class})
    public RestResult<?> exception(Exception e) {
        // 多语言问题
        if (e instanceof CustomException dalException) {
            log.error("全局捕获业务异常-{}", e.getMessage(), e);
            return RestResultFactory.error(dalException.getErrorCode(), dalException.getErrorMsg());
        }
        if (e instanceof NoResourceFoundException neException) {
            if (neException.getResourcePath().contains(ACTUATOR_URI)) {
                log.warn("actuator资源查询异常-{}", neException.getMessage());
                return RestResultFactory.error(CustomError.SYS_ERROR);
            }
            log.error("发生资源查询异常-{}", neException.getMessage(), neException);
        } else {
            log.error("全局捕获其他异常-{}", e.getMessage(), e);
        }
        return RestResultFactory.error(CustomError.SYS_ERROR);
    }

    @Override
    public void afterPropertiesSet() {
        log.info("CustomExceptionHandler init success");
    }
}
