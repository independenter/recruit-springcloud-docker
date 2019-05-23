1.负载均衡
C:/Users/Administrator/.m2/repository/org/springframework/cloud/spring-cloud-openfeign-core/2.1.1.RELEASE/spring-cloud-openfeign-core-2.1.1.RELEASE.jar!/org/springframework/cloud/openfeign/ribbon/LoadBalancerFeignClient.class


RibbonLoadBalancerClient

2.高可用的服务注册中心
127.0.0.1 peer1
127.0.0.1 peer2

3.Hystrix Dashboard
网关服务zuul本来就有，不用额外配置
使用feign调用的服务，需要打开hystrisfeign:hystrix:enabled: true

4.调用HystrixCommand方法，才会处理有hystrix.stream,映射到HystrixMetricsStreamServlet

5.service-turbine靠com.netflix.turbine.monitor.instance.InstanceMonitor解析hystrix.stream的返回

6.predicate篇 请求路径与路由匹配
org.springframework.cloud.gateway.handler.predicate包
AfterRoutePredicateFactory处理After路由配置
优先走代码路由，其次走配置文件路由

7.properties配置list和map
#map 第一种方式
data.person.name=zhangsan
data.person.sex=man
data.person.age=11
data.person.url=xxxxxxxx
#map 第二种方式
data.person[name]=zhangsan
data.person[sex]=man
data.person[age]=11
data.person[url]=xxxxxxxx
#list 第一种方式
data.list[0]=apple0
data.list[1]=apple1
data.list[2]=apple2
#list 第二种方式
data.list=apple0,apple1,apple2

8