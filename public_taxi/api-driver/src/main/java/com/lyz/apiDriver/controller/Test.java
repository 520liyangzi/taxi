package com.lyz.apiDriver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    @GetMapping("/auth")
    public String testAuth(){
        return "auth";
    }

    @GetMapping("/noauth")
    public String testNoAuth(){
        return "noauth";
    }
}
