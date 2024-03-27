package com.itiaoling.metric;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.itiaoling.metric.constants.TagValue;
import org.springframework.http.HttpStatus;

/**
 * @author charles
 * @since 2024/1/10
 */
public class ExceptionUtils {
    public static String getExceptionCode(Throwable throwable) {
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(throwable));
        String errorCode = jsonObject.getString("errorCode");
        if (errorCode != null) {
            return errorCode;
        }
        String errCode = jsonObject.getString("errCode");
        if (errCode != null) {
            return errCode;
        }
        String code = jsonObject.getString("code");
        if (code != null) {
            return code;
        }
        return String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static String getResultCode(String resultPayload) {
        // payload 为空时，返回 OK
        if (resultPayload == null || "".equals(resultPayload.trim())) {
            return TagValue.OK;
        }

        if (!JSON.isValid(resultPayload)) {
            return TagValue.OK;
        }

        JSONObject jsonObject = JSON.parseObject(resultPayload);
        String errorCode = jsonObject.getString("errorCode");
        if (errorCode != null && !errorCode.isEmpty()) {
            return errorCode;
        }

        String errCode = jsonObject.getString("errCode");
        if (errCode != null && !errCode.isEmpty()) {
            return errCode;
        }

        String code = jsonObject.getString("code");
        if (code != null && !code.isEmpty()) {
            if (code.equalsIgnoreCase(String.valueOf(HttpStatus.OK.value()))) {
                return TagValue.OK;
            }
            return code;
        }

        return TagValue.OK;
    }
}
