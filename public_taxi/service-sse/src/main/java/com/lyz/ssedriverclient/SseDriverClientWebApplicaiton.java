package com.lyz.ssedriverclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SseDriverClientWebApplicaiton {
  public static void main(String[] args) {
    //
      SpringApplication.run(SseDriverClientWebApplicaiton.class);
  }
}
