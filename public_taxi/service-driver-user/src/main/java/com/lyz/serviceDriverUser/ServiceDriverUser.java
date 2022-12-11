package com.lyz.serviceDriverUser;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.lyz.serviceDriverUser.mapper")
public class ServiceDriverUser {
    public static void main(String[] args) {
        SpringApplication.run(ServiceDriverUser.class);
    }
}
