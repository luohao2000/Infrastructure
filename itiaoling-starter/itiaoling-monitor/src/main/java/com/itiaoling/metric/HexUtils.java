package com.itiaoling.metric;

import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author charles
 * @since 2024/1/4
 */
public class HexUtils {
    public static String getHexCode(String... keys) {
        Assert.notNull(keys, "keys can not be null");
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            if (key == null || key.isEmpty()) {
                sb.append("null");
            } else {
                sb.append(key);
            }
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(sb.toString().getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
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
