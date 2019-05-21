package com.springcloud.eureka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@Slf4j
@EnableEurekaServer
@SpringBootApplication
public class EurekaApplication {


    public static void main(String[] args) {
        logger.info("Eureka Server Starting ....");
        SpringApplication.run(EurekaApplication.class, args);
        logger.info("Eureka Server Started ....");
    }

}
