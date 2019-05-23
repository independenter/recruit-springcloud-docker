package com.sringcloud.gsgateway;

import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

// tag::code[]
@SpringBootApplication
@EnableConfigurationProperties(UriConfiguration.class)
@RestController
public class GsGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GsGatewayApplication.class, args);
    }

    // tag::route-locator[]
    //curl --dump-header - --header 'Host: www.hystrix.com' http://localhost:8080/test
//    @Bean
//    public RouteLocator myRoutes(RouteLocatorBuilder builder, UriConfiguration uriConfiguration) {
//        String httpUri = uriConfiguration.getHttpbin();
//        return builder.routes()
//                .route(p -> p
//                        .path("/get1")
//                        .filters(f -> f.addRequestHeader("Hello", "World"))
//                        .uri(httpUri))
//                .route(p -> p
//                        .host("*.hystrix.com")
//                        .filters(f -> f
//                                .hystrix(config -> config
//                                        .setName("mycmd")
//                                        .setFallbackUri("forward:/fallback")))
//                        .uri(httpUri))
//                .build();
//    }
    // end::route-locator[]

    //curl localhost:8030/customer/123
    @Bean
    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
        // @formatter:off
        return builder.routes()
                .route(r -> r.path("/customer/**")
                        .filters(f -> f.filter(new RequestTimeFilter())
                                .addResponseHeader("X-Response-Default-Foo", "Default-Bar"))
                        .uri("http://httpbin.org:80/get")
                        .order(0)
                        .id("customer_filter_router")
                )
                .build();
        // @formatter:on
    }

//    @Bean
//    public TokenFilter tokenFilter(){
//        return new TokenFilter();
//    }

    @Bean
    public RequestTimeGatewayFilterFactory elapsedGatewayFilterFactory() {
        return new RequestTimeGatewayFilterFactory();
    }

//    @Bean
//    public HostAddrKeyResolver hostAddrKeyResolver() {
//        return new HostAddrKeyResolver();
//    }
//    @Bean
//    public UriKeyResolver uriKeyResolver() {
//        return new UriKeyResolver();
//    }
//    //用户的维度去限流
//    @Bean
//    KeyResolver userKeyResolver() {
//        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("user"));
//    }

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }

    // tag::fallback[]
    @RequestMapping("/fallback")
    public Mono<String> fallback() {
        return Mono.just("fallback");
    }
    // end::fallback[]
}
// tag::uri-configuration[]
@ConfigurationProperties
@Data
class UriConfiguration {

    private String httpbin="http://httpbin.org";
}
// end::uri-configuration[]
// end::code[]
