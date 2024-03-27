# service-template


api 对外提供接口层 其他服务直接引用该包即可完成接口引入
business 业务实现层
infrastructure 基础架构层 包含 mybatis redis 等
domains 实体类 spi 与 api 实体分开
spi 指的是内部调用的接口,不对外提供服务
web 页面层, api 接口实现也在这里

#### 使用说明

> 使用该模板生成微服务项目之后,要做的事

1. 如果该服务有提供feign接口给其他服务使用,请按需求将模块 -api 下面的包名com.itiaoling.{{serviceName}}中的{{serviceName}}改为具体的服务名,确保唯一,避免后续其他服务调用引用包冲突
2. 修改application.yml中的spring.application.name属性为自己的服务名称和server.port
3. 使用@FeignClient定义feign接口的时候,一定要注意name属性值=spring.application.name的值,否则调不通 
4. 指定实际的 @MapperScan(basePackages = "com.itiaoling.**.mybatis.mapper") 


mvn archetype:create-from-project 使用这个创建骨架包

```
mvn archetype:generate `
-DarchetypeRepository="http://nexus.itiaoling.com" `
-DarchetypeGroupId="com.itiaoling" `
-DarchetypeArtifactId="service-template-archetype" `
-DarchetypeVersion="1.0.0-SNAPSHOT" `
-DgroupId="com.itiaoling" `
-Dversion="1.0.0-SNAPSHOT" `
-DartifactId="wmw-service" `
-Dpackage="com.itiaoling.mwm" `
-DinteractiveMode="false"
```
artifactId 项目名称
package 包名+项目名称 避免后续包冲突
