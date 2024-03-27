package com.itiaoling.dal;


import com.itiaoling.dal.domains.DataAccessKey;
import com.itiaoling.dal.factory.DalFactoryInitializer;
import com.itiaoling.dal.factory.DalFactoryStrategy;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSourceFactoryTest {

    private static final Logger LOG = LoggerFactory.getLogger(DataSourceFactoryTest.class);

    @Test
    public void create() {
        try {
            DataSource dataSource = DalFactoryInitializer.RDB.build(DataAccessKey.generate("first-app", "goods", "goods-read"), DalFactoryStrategy.MYSQL);

            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setDataSource(dataSource);
            HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

            Connection connection = hikariDataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select count(1) from order_sales_order");
            while (resultSet.next()) {
                int result = resultSet.getInt(1);
                LOG.info("esc dev isc-buyer order_sales_order count result:{}", result);
            }
            hikariDataSource.close();
        } catch (SQLException e) {
            LOG.info("exception", e);
        }
    }
}
