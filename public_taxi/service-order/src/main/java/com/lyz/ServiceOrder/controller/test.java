package com.lyz.ServiceOrder.controller;

import com.lyz.ServiceOrder.mapper.OrderInfoMapper;
import com.lyz.ServiceOrder.service.OrderInfoService;
import com.lyz.internalcommon.dto.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class test {

    @PostMapping("/t")
    public String test(){
        System.out.println("ppp");
        return "123";
    }

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @GetMapping("/test-real-time-order/{orderId}")
    public String t(@PathVariable("orderId") long orderId){
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        orderInfoService.dispatchRealTimeOrder(orderInfo);
        return "success";
    }
}
