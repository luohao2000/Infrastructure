package service;

import com.itiaoling.service.template.business.service.interfaces.TestService;
import com.itiaoling.spring.response.RestResult;
import com.itiaoling.spring.response.RestResultFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author charles
 * @since 2023/12/21
 */
@Slf4j
@RestController
@RequestMapping("/v1/test")
public class TestController {

    @GetMapping("/test")
    public void test() {
        log.info("test");
    }

    @Resource
    private TestService testService;

    @GetMapping
    public RestResult<Void> controllerMetrics() {
        testService.test();
        return RestResultFactory.success();
    }
}
