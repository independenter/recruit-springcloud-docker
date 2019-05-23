package com.sringcloud.gsgateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class RequestTimeFilter implements GatewayFilter, Ordered {
    //请求开始时间
    private static final String REQUEST_TIME_BEGIN = "requestTimeBegin";
    //响应体ContentType
    private static final String ORIGINAL_RESPONSE_CONTENT_TYPE="original_response_content_type";

    private static final String ROUTE = "org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayRoute";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        exchange.getAttributes().put(REQUEST_TIME_BEGIN, System.currentTimeMillis());
        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    Long startTime = exchange.getAttribute(REQUEST_TIME_BEGIN);
                    String contentType = exchange.getAttribute(ORIGINAL_RESPONSE_CONTENT_TYPE);
                    Route route = exchange.getAttribute(ROUTE);
                    logger.info(contentType+" : "+route);
                    if (startTime != null) {
                        logger.info(exchange.getRequest().getURI().getRawPath() + ": " + (System.currentTimeMillis() - startTime) + "ms");
                    }
                })
        );

    }

    @Override
    public int getOrder() {
        return 0;
    }
}