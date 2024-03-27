package com.itiaoling.dal.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5 加密工具
 *
 * @author charles
 * @date 2023/10/26
 */
public class Md5Utils {

    /**
     * 获取字符串的MD5散列值
     *
     * @param input 输入字符串
     * @return MD5散列值
     */
    public static String getMd5(String input) {

        try {
            // 创建 MessageDigest 实例
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算输入字节数组的MD5散列值
            byte[] messageDigest = md.digest(input.getBytes());
            // 将字节数组转换为表示散列值的大整数
            BigInteger no = new BigInteger(1, messageDigest);
            // 将大整数转换为16进制字符串
            StringBuilder hashText = new StringBuilder(no.toString(16));
            // 如果哈希字符串长度不够，需要填充0
            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }
            return hashText.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
