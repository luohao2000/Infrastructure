package service;

import com.itiaoling.service.template.api.domains.SampleDO;
import com.itiaoling.spring.response.RestResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * feign示例服务接口
 * name 走服务发现
 * url 走地址解析
 * contextId + name 保证 bean 唯一
 *
 * @author : charles
 * @since : 2022-04-28 15:43
 */
@FeignClient(name = "other-service", url = "server/path/sample", contextId = "unique-context-id-1")
public interface SampleFeignService {

    /**
     * GET方法示例
     *
     * @return String
     */
    @GetMapping("/greet")
    RestResult<SampleDO> greet();

}
