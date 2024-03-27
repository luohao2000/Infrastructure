package com.itiaoling.dal.factory;

import com.itiaoling.dal.domains.DataAccessConfig;
import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;


/**
 * 原生数据源生成策略
 *
 * @author charles
 * @date 2023/10/26
 */
class MysqlDataSourceStrategy extends AbstractDataSourceStrategy {
    @Override
    protected DataSource buildDataSource(DataAccessConfig config) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(config.getUrl());
        dataSource.setUser(config.getUsername());
        dataSource.setPassword(config.getPassword());
        return dataSource;
    }
}
