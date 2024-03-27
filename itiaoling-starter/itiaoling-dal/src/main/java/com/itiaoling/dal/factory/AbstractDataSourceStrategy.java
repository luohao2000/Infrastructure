package com.itiaoling.dal.factory;

import com.itiaoling.dal.domains.DataAccessConfig;
import com.itiaoling.dal.domains.DataAccessKey;
import com.itiaoling.dal.factory.interfaces.Factory;
import com.itiaoling.dal.factory.interfaces.FactoryStrategy;
import com.itiaoling.dal.utils.Md5Utils;
import com.itiaoling.json.JsonUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Map;

/**
 * data source 生产策略 提供原生数据源
 *
 * @author charles
 * @date 2023/10/26
 */
public abstract class AbstractDataSourceStrategy implements FactoryStrategy<DataSource> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractDataSourceStrategy.class);

    @Override
    public DataSource init(DataAccessKey key) {
        DataAccessConfig config = fetchRemoteConfig(key);
        if (config == null) {
            throw new RuntimeException("Itiaoling dal fetch remote config failed");
        }
        return buildDataSource(config);
    }

    @Override
    public boolean evaluate(Factory<DataSource> factory) {
        return factory instanceof DataSourceFactory;
    }

    /**
     * 获取远程配置
     *
     * @param key 键值
     * @return DataAccessConfig
     */
    protected DataAccessConfig fetchRemoteConfig(DataAccessKey key) {
        boolean keyValid = DataAccessKey.validateKey(key);
        if (!keyValid) {
            throw new RuntimeException("Itiaoling dal fetch remote config error key is invalid");
        }
        String jsonKey = JsonUtil.toJson(key);
        if (jsonKey == null || jsonKey.length() == 0) {
            throw new RuntimeException("Itiaoling dal fetch remote config error jsonKey is null");
        }

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("http://system-dal.mp-tech/config/fetch");
            httpPost.addHeader("appId", key.getAppId());
            httpPost.addHeader("authorization", Md5Utils.getMd5(key.joinDot()));
            httpPost.addHeader("content-type", "application/json");
            httpPost.addHeader("accept", "application/json");

            StringEntity entity = new StringEntity(jsonKey);
            httpPost.setEntity(entity);
            CloseableHttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String dataString = EntityUtils.toString(responseEntity);
                Map<String, String> map = JsonUtil.fromJsonToMap(dataString, String.class);
                String result = map.get("result");
                if (result == null) {
                    LOG.error("Itiaoling dal fetch remote config error result is null map:{}", map);
                    throw new RuntimeException("Itiaoling dal fetch remote config error result is null");
                }
                DataAccessConfig dataAccessConfig = JsonUtil.fromJson(result, DataAccessConfig.class);
                if (dataAccessConfig == null) {
                    throw new RuntimeException("Itiaoling dal fetch remote config error dataAccessConfig is null");
                }
                dataAccessConfig.decryptNeeded();
                return dataAccessConfig;
            } else {
                throw new RuntimeException("Itiaoling dal fetch remote config error request failed");
            }
        } catch (Exception e) {
            LOG.error("Itiaoling dal fetch remote config error", e);
            throw new RuntimeException("Itiaoling dal fetch remote config error");
        }
    }

    /**
     * 构建数据源
     *
     * @param config 配置
     * @return DataSource
     */
    protected abstract DataSource buildDataSource(DataAccessConfig config);


}
