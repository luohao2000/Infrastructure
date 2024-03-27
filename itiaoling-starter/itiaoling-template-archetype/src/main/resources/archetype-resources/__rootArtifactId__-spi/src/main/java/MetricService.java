#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

/**
 * 指标服务
 *
 * @author charles
 * @since 2023/12/15
 */
public interface MetricService {

    /**
     * mq 发送指标
     *
     * @param message 消息
     */
    void send(String message);
}
