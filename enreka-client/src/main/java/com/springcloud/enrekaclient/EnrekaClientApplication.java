package com.springcloud.enrekaclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@Slf4j
@EnableEurekaClient
@RestController
public class EnrekaClientApplication {

    public static void main(String[] args) {
        logger.info("EnrekaClientApplication Starting ...");
        SpringApplication.run(EnrekaClientApplication.class, args);
        logger.info("EnrekaClientApplication Started ...");
    }

    @Value("${server.port}")
    String port;

    @RequestMapping("/hi")
    public String home(@RequestParam(value = "name", defaultValue = "server") String name) {
        return "hi " + name + " ,i am from port:" + port;
    }


}
