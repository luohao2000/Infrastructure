package com.itiaoling.dal.domains;

import com.google.common.base.Objects;
import com.itiaoling.dal.utils.AesUtils;
import com.itiaoling.dal.utils.Md5Utils;

import java.io.Serializable;
import java.util.Map;

/**
 * 数据库配置信息
 *
 * @author charles
 * @date 2023/10/25
 */
public class DataAccessConfig implements Serializable {
    private static final long serialVersionUID = 7460322237314219534L;

    /**
     * dataAccessKey
     */
    private DataAccessKey key;

    /**
     * 数据库 url
     */
    private String url;

    /**
     * 数据库用户名
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password;

    public DataAccessKey getKey() {
        return key;
    }

    public void setKey(DataAccessKey key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataAccessConfig)) {
            return false;
        }
        DataAccessConfig that = (DataAccessConfig) o;
        return Objects.equal(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }

    /**
     * 解密
     */
    public void decryptNeeded() {
        String key = Md5Utils.getMd5(this.key.joinSnake());
        if (url != null) {
            url = AesUtils.decrypt(url, key);
        }

        if (username != null) {
            username = AesUtils.decrypt(username, key);
        }

        if (password != null) {
            password = AesUtils.decrypt(password, key);
        }

    }

    /**
     * 加密
     */
    public void encryptNeeded() {
        String key = Md5Utils.getMd5(this.key.joinSnake());

        if (url != null) {
            url = AesUtils.encrypt(url, key);
        }

        if (username != null) {
            username = AesUtils.encrypt(username, key);
        }

        if (password != null) {
            password = AesUtils.encrypt(password, key);
        }
    }

    public static DataAccessConfig fromConfigMap(String prefix,Map<String, String> map) {
        if(map == null || map.isEmpty()){
            return null;
        }

        DataAccessConfig dataAccessConfig = new DataAccessConfig();
        map.forEach((k,v)->{
            if(k.startsWith(prefix)){
                String key = k.substring(prefix.length());
                switch (key){
                    case "url":
                        dataAccessConfig.setUrl(v);
                        break;
                    case "username":
                        dataAccessConfig.setUsername(v);
                        break;
                    case "password":
                        dataAccessConfig.setPassword(v);
                        break;
                    default:
                        break;
                }
            }
        });
        dataAccessConfig.encryptNeeded();
        return dataAccessConfig;
    }
}
