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
org.springframework.cloud.gateway.filter.factory包
org.springframework.cloud.gateway.filter包
org.springframework.cloud.gateway.handler.FilteringWebHandler 执行顺序
由filter工作流程点，可以知道filter有着非常重要的作用，在“pre”类型的过滤器可以做参数校验、权限校验、流量监控、日志输出、协议转换等，
在“post”类型的过滤器中可以做响应内容、响应头的修改，日志的输出，流量监控等。首先需要弄清一点为什么需要网关这一层，这就不得不说下filter的作用了。
# AddRequestHeaderGatewayFilterFactory
# global filter
 Spring Cloud Gateway根据作用范围划分为GatewayFilter和GlobalFilter，二者区别如下：
 GatewayFilter : 需要通过spring.cloud.routes.filters 配置在具体路由下，只作用在当前路由上或通过spring.cloud.default-filters配置在全局，作用在所有路由上
 GlobalFilter : 全局过滤器，不需要在配置文件中配置，作用在所有的路由上，最终通过GatewayFilterAdapter包装成GatewayFilterChain可识别的过滤器，
 它为请求业务以及路由的URI转换为真实业务服务的请求地址的核心过滤器，不需要配置，系统初始化时加载，并作用在每个路由上。
 
 10.限流
 常见的限流算法
 #计数器算法
 计数器算法采用计数器实现限流有点简单粗暴，一般我们会限制一秒钟的能够通过的请求数，比如限流qps为100，
 算法的实现思路就是从第一个请求进来开始计时，在接下去的1s内，每来一个请求，就把计数加1，如果累加的数字
 达到了100，那么后续的请求就会被全部拒绝。等到1s结束后，把计数恢复成0，重新开始计数。具体的实现可以是
 这样的：对于每次服务调用，可以通过AtomicLong#incrementAndGet()方法来给计数器加1并返回最新值，通过
 这个最新值和阈值进行比较。这种实现方式，相信大家都知道有一个弊端：如果我在单位时间1s内的前10ms，已经
 通过了100个请求，那后面的990ms，只能眼巴巴的把请求拒绝，我们把这种现象称为“突刺现象”
 #漏桶算法
 在算法实现方面，可以准备一个队列，用来保存请求，另外通过一个线程池（ScheduledExecutorService）来
 定期从队列中获取请求并执行，可以一次性获取多个并发执行。
 这种算法，在使用过后也存在弊端：无法应对短时间的突发流量。
 #令牌桶算法
 从某种意义上讲，令牌桶算法是对漏桶算法的一种改进，桶算法能够限制请求调用的速率，而令牌桶算法能够在限制
 调用的平均速率的同时还允许一定程度的突发调用。在令牌桶算法中，存在一个桶，用来存放固定数量的令牌。算法
 中存在一种机制，以一定的速率往桶中放令牌。每次请求调用需要先获取令牌，只有拿到令牌，才有机会继续执行，
 否则选择选择等待可用的令牌、或者直接拒绝。放令牌这个动作是持续不断的进行，如果桶中令牌数达到上限，就丢
 弃令牌，所以就存在这种情况，桶中一直有大量的可用令牌，这时进来的请求就可以直接拿到令牌执行，比如设置qps
 为100，那么限流器初始化完成一秒后，桶中就已经有100个令牌了，这时服务还没完全启动好，等启动完成对外提供
 服务时，该限流器可以抵挡瞬时的100个请求。所以，只有桶中没有令牌时，请求才会进行等待，最后相当于以一定的
 速率执行。
 实现思路：可以准备一个队列，用来保存令牌，另外通过一个线程池定期生成令牌放到队列中，每来一个请求，就从队
 列中获取一个令牌，并继续执行
 #Spring Cloud Gateway限流  RequestRateLimiterGatewayFilterFactory
 在Spring Cloud Gateway中，有Filter过滤器，因此可以在“pre”类型的Filter中自行实现上述三种过滤器。
 但是限流作为网关最基本的功能，Spring Cloud Gateway官方就提供了RequestRateLimiterGatewayFilterFactory
 这个类，适用Redis和lua脚本实现了令牌桶的方式。具体实现逻辑在RequestRateLimiterGatewayFilterFactory类中，
 lua脚本在如下图所示的文件夹中：
 C:/Users/Administrator/.m2/repository/org/springframework/cloud/spring-cloud-gateway-core/2.1.1.RELEASE/spring-cloud-gateway-core-2.1.1.RELEASE.jar!/META-INF/scripts/request_rate_limiter.lua

 11.Jmeter Plugins提供了五类组件：
 #Standard Set:对线程组进行了扩展，扩充了监听器，更丰富了图标的展示
 #Extras Set:监听器进一步扩展，支持远程监控，图表展示更加丰富
 #Extras with Libs Set:提供了对JSON的支持，新增了JMS取样器
 #WebDriver Set:与WebDriver进行了集成，来进行自动化测试
 #Hadoop Set:提供了Hadoop测试组件

 12.http://localhost:8030/foo/independenter?token=32
 redis没有缓存key