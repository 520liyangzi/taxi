package com.lyz.apipassenger.controller;

import com.lyz.apipassenger.remote.ServiceOrderClient;
import com.lyz.internalcommon.dto.OrderInfo;
import com.lyz.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test(){
        return "test api passenger";
    }

    @GetMapping("/authTest")
    public ResponseResult authTest(){
        //需要token
        System.out.println("need");
        return ResponseResult.success("authTest");
    }

    @GetMapping("/noAuthTest")
    public ResponseResult noAuthTest(){
        System.out.println("no need");
        return ResponseResult.success("noAuthTest");
    }

    @Autowired
    ServiceOrderClient serviceOrderClient;

    @GetMapping("/test-real-time-order/{orderId}")
    public ResponseResult t(@PathVariable("orderId") long orderId){
        System.out.println("并发测试： orderId" + orderId + "端口号 :");
        ResponseResult t = serviceOrderClient.t(orderId);
        return ResponseResult.success();
    }
}
