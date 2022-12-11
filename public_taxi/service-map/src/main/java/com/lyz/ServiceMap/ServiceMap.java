package com.lyz.ServiceMap;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@MapperScan("com.lyz.ServiceMap.mapper")
@EnableDiscoveryClient
@SpringBootApplication
public class ServiceMap {
    public static void main(String[] args) {
        SpringApplication.run(ServiceMap.class);
    }
    @Bean
    public RestTemplate restTemplate(){
       return new RestTemplate();
    }
}
