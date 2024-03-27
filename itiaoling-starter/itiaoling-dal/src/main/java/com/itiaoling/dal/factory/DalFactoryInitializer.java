package com.itiaoling.dal.factory;

import com.itiaoling.dal.factory.interfaces.Factory;
import com.mongodb.client.MongoClient;

import javax.sql.DataSource;


/**
 * 工厂对象提供者
 *
 * @author charles
 * @date 2023/10/26
 */
public class DalFactoryInitializer {
    /**
     * 关系型数据库数据源工厂
     */
    public static final Factory<DataSource> RDB = new DataSourceFactory();

    /**
     * todo 待迭代
     */
    private static final Factory<MongoClient> MONGO = new MongoClientFactory();


}
