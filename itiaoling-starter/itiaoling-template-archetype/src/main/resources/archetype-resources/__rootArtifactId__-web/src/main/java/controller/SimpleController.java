#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller;

import ${package}.MetricService;
import ${package}.spring.response.RestResult;
import ${package}.spring.response.RestResultFactory;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author charles
 * @since 2023/12/14
 */
@RestController
@RequestMapping("/simple")
public class SimpleController {

    @Resource
    private MetricService metricService;

    @GetMapping("/get")
    public RestResult<Void> get() {
        metricService.send("hello");
        return RestResultFactory.success();
    }
}
