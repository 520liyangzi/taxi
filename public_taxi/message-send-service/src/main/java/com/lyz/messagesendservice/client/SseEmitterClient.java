package com.lyz.messagesendservice.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-sse")
public interface SseEmitterClient {

    @RequestMapping(method = RequestMethod.GET,value = "/push")
    String push(@RequestParam Long userId, @RequestParam String identity, @RequestParam String content);
}
