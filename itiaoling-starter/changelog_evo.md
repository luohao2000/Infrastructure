# 开发日志

## release-evo 1.1.4

### itiaoling-monitor

* 增加 prometheus pushgateway 0.12.0
* 增加 sdk pushgate way 上报 能力
* 重构 monitor 出口确认为 TaggedMonitor 与 MonitorExecutor

### itiaoling-spring

* 增加统一异常信息与统一返回信息定义
* 增加 LegacyResponse 兼容历史 vos 接口通用返回

### itiaoling-spring-boot-starter

* 增加默认 spring-boot-web 依赖

* 增加 CustomExceptionHandler 全局异常处理

* 增加线程池

* 引入 itiaoling-monitor

* 增加 MonitorFilter 与 MonitorAspect（可选）自动输出出口日志与上报指标

  

## release-evo 1.1.3

### itiaoling-logs

* 修复计时器无法正确输出请求响应耗时。
* 修复入参出参类型打印为空。
* 优化脱敏逻辑，非脱敏字段直接序列化。
*  修复启动时日志组件本身输出信息递归导致卡死问题。
* 优化 elk appender 首次启动逻辑，环境就绪启动。
* 增加默认日志信息 encoder 保证 控制台日志与 kafka 日志结构一致。
* 增加默认 logback-itiaoling.xml 用于后续统一业务线日志模板
* 修复日志组件 debug 输出

### itiaoling-dal

* 首次提交业务线获取数据库数据源能力,Alpha版本不建议生产环境使用。

### itiaoling-monitor

* 修复启动时依赖问题。

