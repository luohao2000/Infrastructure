# 开发日志

## release-hoxton 1.0.3

### itiaoling-logs

* 修复计时器无法正确输出请求响应耗时。
*  修复入参出参类型打印为空。
* 优化脱敏逻辑，非脱敏字段直接序列化。
* 修复启动时日志组件本身输出信息递归导致卡死问题。
* 优化 elk appender 首次启动逻辑，环境就绪启动。
* 增加默认日志信息 encoder 保证 控制台日志与 kafka 日志结构一致。
* 增加默认 logback-itiaoling.xml 用于后续统一业务线日志模板。
* 优化 properties 配置提示

### itiaoling-dal

* 首次提交业务线获取数据库数据源能力,Alpha版本不建议生产环境使用。

### itiaoling-monitor

* 修复启动时依赖问题。