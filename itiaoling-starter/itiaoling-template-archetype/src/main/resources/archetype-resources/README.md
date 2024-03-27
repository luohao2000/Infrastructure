# system-dal


api 对外提供接口层
business 业务实现层层
infrastructure 基础架构层
pojo 实体类
spi 指的是内部调用的接口,不对外提供服务
web 页面层

#### 使用说明

> 使用该模板生成微服务项目之后,要做的事

1. 指定 spring.application.name 与 maven pom artifactId 
2. 项目模块新建 artifactId 对应的包，目前包结构只到 com.itiaoling，应该到 com.itiaoling.${artifactId}
3. 如果该服务有提供feign接口给其他服务使用,请按需求将模块 template-api 下面的包名com.itiaoling.{{serviceName}}改为具体的服务名,确保唯一,避免后续其他服务调用引用包冲突
4. 修改application.yml中的spring.application.name属性为自己的服务名称和server.port
5. 建议修改SampleServiceApplication名称为指定服务名称
6. 使用@FeignClient定义feign接口的时候,一定要注意name属性值=spring.application.name的值,考虑使用 context-id 分化服务内容