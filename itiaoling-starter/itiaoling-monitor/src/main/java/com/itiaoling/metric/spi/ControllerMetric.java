package com.itiaoling.metric.spi;

import lombok.Data;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author charles
 * @since 2023/12/27
 */
@Data
public class ControllerMetric {
    /**
     * 应用名称
     */
    private String appName;

    /**
     * 描述
     */
    private String description;

    /**
     * 请求地址
     */
    private String url;

    /**
     * http 方法
     */
    private String httpMethod;

    /**
     * 请求结果
     */
    private String httpStatus;

    /**
     * 业务响应代码
     */
    private String bizCode;

    /**
     * 数据中心
     */
    private String scenario;

    /**
     * 请求耗时
     */
    private Long duration;

    /**
     * 唯一码 用于分配计时器
     */
    private String uniqueCode;

    /**
     * 生成唯一码
     *
     * @param dataCenter 数据中心
     */
    public void setUniqueCode(String scenario, String dataCenter) {
        Assert.notNull(scenario, "scenario can not be null");
        Assert.notNull(dataCenter, "dataCenter can not be null");
        String input = scenario + dataCenter + appName + url + httpMethod + httpStatus + bizCode;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            this.uniqueCode = bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
