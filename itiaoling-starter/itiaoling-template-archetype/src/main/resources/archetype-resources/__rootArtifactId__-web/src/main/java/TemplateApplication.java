#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// @EnableFeignClients(basePackageClasses = {})
// @EnableConfigurationProperties
// @EnableAsync
// @MapperScan(basePackages = "com.ocgsc.mybatis.mapper")
/**
 * @author charles
 * @since 2023/10/26
 */
// @EnableRabbit
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"${package}"})
public class TemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
    }
}
