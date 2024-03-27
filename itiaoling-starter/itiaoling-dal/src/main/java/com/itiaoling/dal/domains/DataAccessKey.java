package com.itiaoling.dal.domains;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 数据库配置对应键值
 *
 * @author charles
 * @date 2023/10/25
 */
public class DataAccessKey implements Serializable {

    private static final long serialVersionUID = -5537921046701515476L;

    private static final Logger LOG = LoggerFactory.getLogger(DataAccessKey.class);

    /**
     * 分组信息
     */
    private final String group;

    /**
     * 应用 id
     */
    private final String appId;

    /**
     * 应用下附属 db 键值
     */
    private final String subKey;

    DataAccessKey(String group, String appId, String subKey) {
        this.group = group;
        this.appId = appId;
        this.subKey = subKey;
    }

    /**
     * 生成键值
     *
     * @param appId  应用 id
     * @param subKey 应用下附属 db 键值
     * @return DataAccessKey
     */
    @JsonCreator
    public static DataAccessKey generate(@JsonProperty("group") String group, @JsonProperty("appId") String appId, @JsonProperty("subKey") String subKey) {
        if (appId == null || appId.length() == 0) {
            throw new RuntimeException("DataAccessKey generate error appId is null");
        }
        if (subKey == null || subKey.length() == 0) {
            throw new RuntimeException("DataAccessKey generate error subKey is null");
        }

        return new DataAccessKey(group, appId, subKey);
    }


    public String joinDot() {
        return appId + "." + subKey;
    }

    public String joinSnake() {
        return appId + "_" + subKey;
    }

    public String getAppId() {
        return appId;
    }

    public String getSubKey() {
        return subKey;
    }

    public String getGroup() {
        return group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataAccessKey)) {
            return false;
        }
        DataAccessKey that = (DataAccessKey) o;
        return Objects.equal(group, that.group) && Objects.equal(appId, that.appId) && Objects.equal(subKey, that.subKey);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(group, appId, subKey);
    }

    @Override
    public String toString() {
        return "{\"appId\":\"" + appId + "\"," +
                " \"subKey\":\"" + subKey + "\"" +
                '}';
    }

    /**
     * validate key success true else throw runtime exception
     *
     * @param key key
     * @return boolean
     */
    public static boolean validateKey(DataAccessKey key) {
        if (key == null) {
            LOG.error("Itiaoling dal fetch remote config error key is null");
            return false;
        }
        if (key.getGroup() == null) {
            LOG.error("Itiaoling dal fetch remote config error group is null");
            return false;
        }
        if (key.getAppId() == null) {
            LOG.error("Itiaoling dal fetch remote config error appId is null");
            return false;
        }
        if (key.getSubKey() == null) {
            LOG.error("Itiaoling dal fetch remote config error env is null");
            return false;
        }
        return true;
    }
}

