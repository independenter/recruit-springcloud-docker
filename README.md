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

8.gateway 位于代理机后面的情景
https://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.0.0.RELEASE/single/spring-cloud-gateway.html
#4.10.1 Modifying the way remote addresses are resolved

9。filter的作用和生命周期
由filter工作流程点，可以知道filter有着非常重要的作用，在“pre”类型的过滤器可以做参数校验、权限校验、流量监控、日志输出、协议转换等，
在“post”类型的过滤器中可以做响应内容、响应头的修改，日志的输出，流量监控等。首先需要弄清一点为什么需要网关这一层，这就不得不说下filter的作用了。
# AddRequestHeaderGatewayFilterFactory
# global filter
 Spring Cloud Gateway根据作用范围划分为GatewayFilter和GlobalFilter，二者区别如下：
 GatewayFilter : 需要通过spring.cloud.routes.filters 配置在具体路由下，只作用在当前路由上或通过spring.cloud.default-filters配置在全局，作用在所有路由上
 GlobalFilter : 全局过滤器，不需要在配置文件中配置，作用在所有的路由上，最终通过GatewayFilterAdapter包装成GatewayFilterChain可识别的过滤器，
 它为请求业务以及路由的URI转换为真实业务服务的请求地址的核心过滤器，不需要配置，系统初始化时加载，并作用在每个路由上。