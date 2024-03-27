# 开发日志

## release-delta 1.2.5

### itiaoling-spring-boot-starter

* 全局异常处理优化监控接口异常上报
* 引入 itiaoling-monitor
* 增加 MonitorFilter 与 MonitorAspect（可选）自动输出出口日志与上报指标

### itiaoling-monitor

* 调整 prometheus 桥接器版本 1.12.0
* 增加 prometheus pushgateway 0.16.0
* 增加 sdk pushgate way 上报 能力
* 重构 monitor 出口确认为 TaggedMonitor 与 MonitorExecutor

### itiaoling-logs 

* 增加链路追踪能力

## release-delta 1.2.4

fork from release-evo 1.1.4

### itiaoling-logs

* 优化 properties 配置提示
* 减少内部日志无效日志产生

### itiaoling-spring

* 增加统一异常信息与统一返回信息定义

### itiaoling-spring-boot-starter

* 增加默认 spring-boot-web 依赖
* 增加 CustomExceptionHandler 全局异常处理
* 增加 i18n 支持
* 增加 bootstrap 配置文件支持
* 增加统一 logback-itiaoling.xml
* 增加默认线程池工具