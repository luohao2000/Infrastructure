package com.itiaoling.dal.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @author charles
 * @date 2023/10/26
 */
public class AesUtils {

    private static final Logger LOG = LoggerFactory.getLogger(AesUtils.class);

    /**
     * 使用AES算法
     */
    private static final String ALGORITHM = "AES";

    /**
     * 使用AES加密算法和ECB工作模式
     */
    private static final String MODE = "AES/ECB/PKCS5Padding";

    /**
     * 加密
     *
     * @param data 待加密内容
     * @param key  加密秘钥
     * @return 加密后的内容
     */
    public static String encrypt(String data, String key) {
        try {

            Cipher cipher = Cipher.getInstance(MODE);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            LOG.error("Itiaoling Dal encrypt error", e);
            return data;
        }
    }

    /**
     * 解密
     *
     * @param encryptedData 待解密内容
     * @param key           解密秘钥
     * @return 解密后的内容
     */
    public static String decrypt(String encryptedData, String key) {
        try {
            Cipher cipher = Cipher.getInstance(MODE);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        } catch (Exception e) {
            LOG.error("Itiaoling Dal decrypt error", e);
            return encryptedData;
        }
    }
}
