# itiaoling 基层框架

## 版本基线

### release-delta

基础版本号 1.2.x

* super-pom 1.2.x
* spring-boot-starter 3.2.0
* jdk 17

### release-evo

基础版本号 1.1.x

* super-pom 1.1.x
* spring-boot-starter 2.6.15
* jdk 8

### release-hoxton

基础版本号 1.0.x

* super-pom 1.0.x
* spring-boot-starter 2.3.12
* jdk 8

## 模块划分

### itiaoling-dal

data access layer 用于管理数据库连接信息

### itiaoling-json

封装底层 json 操作

### itiaoling-logs

日志模块支持

* 提供 elk 日志输出
* 提供类/方法环绕输入输出信息

### itiaoling-monitor

监控模块支持

* Prometheus pushgateway 主动上报
* Prometheus actuator 数据收集

#### pushgateway

```java
public void sendControllerMetrics(ProbeMetricRequest<ProbeControllerMetric> metrics) {
        String applicationName = metrics.getApplicationName();
        String scenario = metrics.getScenario();
        String dataCenter = metrics.getDataCenter();
        List<ProbeControllerMetric> probeMetrics = metrics.getProbeMetrics();
        List<Tag> extraTags = List.of(
                new ImmutableTag("scenario", scenario),
                new ImmutableTag("data_center", dataCenter));
        MetricProvider provider = MetricProviderManager.get(applicationName);

        probeMetrics.stream()
                .map(t -> {
                    ControllerMetric controllerMetric = new ControllerMetric();
                    controllerMetric.setAppName(applicationName);
                    controllerMetric.setDescription(t.getDescription());

                    controllerMetric.setUrl(t.getUrl());
                    controllerMetric.setDuration(t.getDuration());
                    controllerMetric.setHttpMethod(t.getHttpMethod());
                    controllerMetric.setHttpStatus(t.getHttpStatus());
                    if (String.valueOf(HttpStatus.OK.value()).equals(controllerMetric.getHttpStatus())) {
                        if (t.getBizCode() == null) {
                            controllerMetric.setBizCode(NULL);
                        } else {
                            controllerMetric.setBizCode(t.getBizCode());
                        }
                    } else {
                        controllerMetric.setBizCode(NULL);
                    }
                    controllerMetric.setUniqueCode(scenario, dataCenter);
                    return controllerMetric;
                })
                // 遍历收集指标
                .forEach(t -> provider.controllerMetrics(t, extraTags));

        if (probeSdkProperties.isDebug()) {
            PrometheusMeterRegistry meterRegistry = (PrometheusMeterRegistry) provider.get();
            log.info("collector metrics:\n{}", meterRegistry.scrape());
        }

        // 触发推送
        provider.pushAdd(probeSdkProperties.getGatewayUrl());
        if (probeSdkProperties.isDebug()) {
            log.info("controller metrics success,gateway url:{}", probeSdkProperties.getGatewayUrl());
        }
    }
```

#### spi

```java
    @Resource
    private Monitor taggedMonitor;
    
    private void runWithMonitor(){
    	        try {
            List<Tag> tagList = new ArrayList<>();
            tagList.add(new ImmutableTag(TagName.HTTP_METHOD, HttpMethod.POST.name()));
            tagList.add(new ImmutableTag(TagName.HTTP_STATUS, String.valueOf(Optional.ofNullable(response).map(HttpResponse::getStatus)
                    .orElse(HttpStatus.INTERNAL_SERVER_ERROR.value()))));
            tagList.add(new ImmutableTag(TagName.URL, requestUrl));
            if (caughtException == null) {
                tagList.add(new ImmutableTag(TagName.BIZ_CODE, ExceptionUtils.getResultCode(body)));
            } else {
                tagList.add(new ImmutableTag(TagName.BIZ_CODE, ExceptionUtils.getExceptionCode(caughtException)));
            }

            tagList.add(new ImmutableTag(TagName.SOURCE, getClass().getSimpleName()));
            tagList.add(new ImmutableTag(TagName.PRODUCT_LINE, appProperties.getProductLine()));
            tagList.add(new ImmutableTag(TagName.DATA_CENTER, appProperties.getDataCenter()));
            taggedMonitor.record(tagList, responseTime - requestTime, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.error("monitor error", e);
    }
```

### itiaoling-spring

* 基础 spring 上下文支持
* 通用异常封装 CustomException
* 通用响应结果封装 RestResult

### itiaoling-spring-boot-starter

* 统一 starter 集成已有框架
* 集成全局异常处理
* i18n 集成
* 统一 logback 引入
* 线程池工具

### itiaoling-template-archetype

* 通用项目模板

* 通用项目模板生成脚本
  通过 cmd

  ```shell
  mvn archetype:generate \
  -DarchetypeCatalog=local \
  -DarchetypeGroupId=com.itiaoling \
  -DarchetypeArtifactId=itiaoling-template-archetype \
  -DarchetypeVersion=1.2.4-SNAPSHOT \
  -DgroupId=com.yourcompany \
  -DartifactId=your-artifact \
  -Dversion=1.0.0-SNAPSHOT \
  -DinteractiveMode=false 
  
  ```

或者 idea Run Configurations 增加 右侧 maven/itiaoling-template-archetype/plugins/archetype/archetype:generate
右键 modify run configuration 改为这个 执行即可

```
archetype:generate -DarchetypeCatalog=local -DarchetypeGroupId=com.itiaoling -DarchetypeArtifactId=itiaoling-template-archetype -DarchetypeVersion=1.2.4-SNAPSHOT -DgroupId=com.itiaoling -DartifactId=generate-first -Dversion=1.0.0-SNAPSHOT -DinteractiveMode=false -f pom.xml
```
