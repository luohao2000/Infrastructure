#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

/**
 * feign示例服务接口
 *
 * @author : charles
 * @since : 2022-04-28 15:43
 */
public interface IFeignSampleService {

    /**
     * GET方法示例
     *
     * @return String
     */
    // @GetMapping("/sayHi")
    String sayHi();

}
