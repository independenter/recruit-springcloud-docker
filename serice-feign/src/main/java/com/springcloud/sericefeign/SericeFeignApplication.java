package com.springcloud.sericefeign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
@Slf4j
public class SericeFeignApplication {

    public static void main(String[] args) {
        logger.info("ServiceFeignApplication Starting ... ");
        SpringApplication.run(SericeFeignApplication.class, args);
        logger.info("ServiceFeignApplication Starting ... ");
    }

}
