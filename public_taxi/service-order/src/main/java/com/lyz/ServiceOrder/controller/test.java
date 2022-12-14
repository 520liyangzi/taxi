package com.lyz.ServiceOrder.controller;

import com.lyz.ServiceOrder.mapper.OrderInfoMapper;
import com.lyz.ServiceOrder.service.OrderInfoService;
import com.lyz.internalcommon.dto.OrderInfo;
import com.lyz.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${server.port}")
    String port;

    @GetMapping("/test-real-time-order/{orderId}")
    public ResponseResult t(@PathVariable("orderId") long orderId){
        System.out.println("并发测试： orderId" + orderId + "端口号 :"+ port);
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        orderInfoService.dispatchRealTimeOrder(orderInfo);
        return ResponseResult.success();
    }
}
