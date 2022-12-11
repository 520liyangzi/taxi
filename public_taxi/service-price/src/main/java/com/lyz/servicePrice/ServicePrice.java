package com.lyz.servicePrice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@MapperScan("com.lyz.servicePrice.mapper")
public class ServicePrice {
    public static void main(String[] args) {
        SpringApplication.run(ServicePrice.class);
    }
}
