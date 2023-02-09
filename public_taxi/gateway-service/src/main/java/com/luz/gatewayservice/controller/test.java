package com.luz.gatewayservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {

    @GetMapping("/ttt")
    public String t(){
    System.out.println("111");
        return "qq";
    }
}
