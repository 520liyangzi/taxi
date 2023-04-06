package com.lyz.messagesendservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {


    @GetMapping("/t")
    public String test(){
    System.out.println("1");
    return "ttt";
    }
}
