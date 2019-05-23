1.负载均衡
C:/Users/Administrator/.m2/repository/org/springframework/cloud/spring-cloud-openfeign-core/2.1.1.RELEASE/spring-cloud-openfeign-core-2.1.1.RELEASE.jar!/org/springframework/cloud/openfeign/ribbon/LoadBalancerFeignClient.class


RibbonLoadBalancerClient

2.高可用的服务注册中心
127.0.0.1 peer1
127.0.0.1 peer2

3.Hystrix Dashboard
网关服务zuul本来就有，不用额外配置
使用feign调用的服务，需要打开hystrisfeign:hystrix:enabled: true

4.调用HystrixCommand方法，才会处理爱hystrix.stream