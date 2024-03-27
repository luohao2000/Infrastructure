package service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * @author charles
 * @since 2023/10/26
 */
@EnableRabbit
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.itiaoling"}, exclude = {DataSourceAutoConfiguration.class})
@MapperScan(basePackages = "com.itiaoling.**.mybatis.mapper")
public class ServiceTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceTemplateApplication.class, args);
    }
}
