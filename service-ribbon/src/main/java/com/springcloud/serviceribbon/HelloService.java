package com.springcloud.serviceribbon;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HelloService {

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "hiError")
    public String hiService(String name) {
       // ParameterizedTypeReference<String> paramType = new ParameterizedTypeReference<String>() {};
         //加上负载均衡会使改方法失效
//        return restTemplate.getForObject("http://localhost:8002/hi?name="+name,String.class);
//        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8002/hi?name="+name, HttpMethod.GET, null, paramType);
//        return responseEntity.getBody();
        return restTemplate.getForObject("http://EUREKA-CLIENT/hi?name="+name,String.class);
    }

    public String hiError(String name) {
        return "hi,"+name+",sorry,error!";
    }
}