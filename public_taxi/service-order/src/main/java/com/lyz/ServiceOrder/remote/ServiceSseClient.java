package com.lyz.ServiceOrder.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-sse")
public interface ServiceSseClient {

    @RequestMapping(method = RequestMethod.GET,value = "/push")
    String push(@RequestParam Long userId, @RequestParam String identity, @RequestParam String content);
}
