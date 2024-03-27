package com.itiaoling.dal.factory;

import com.itiaoling.dal.factory.interfaces.FactoryStrategy;

import javax.sql.DataSource;


/**
 * 数据源工厂策略
 *
 * @author charles
 * @date 2023/12/4
 */
public class DalFactoryStrategy {

    /**
     * mysql数据源工厂策略
     */
    public static final FactoryStrategy<DataSource> MYSQL = new MysqlDataSourceStrategy();


}
