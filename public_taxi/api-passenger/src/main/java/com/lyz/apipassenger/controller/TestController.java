package com.lyz.apipassenger.controller;

import com.lyz.internalcommon.dto.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
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
}
