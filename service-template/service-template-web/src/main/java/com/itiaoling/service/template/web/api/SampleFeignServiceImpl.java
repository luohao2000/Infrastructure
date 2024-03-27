package service;

import com.itiaoling.service.template.api.SampleFeignService;
import com.itiaoling.service.template.api.domains.SampleDO;
import com.itiaoling.spring.response.RestResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author charles
 * @since 2024/03/05
 */
@RestController
@RequestMapping("/sample")
public class SampleFeignServiceImpl implements SampleFeignService {

    @Override
    public RestResult<SampleDO> greet() {
        return null;
    }
}
