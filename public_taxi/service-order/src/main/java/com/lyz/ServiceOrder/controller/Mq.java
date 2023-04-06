package com.lyz.ServiceOrder.controller;

import com.lyz.ServiceOrder.mapper.OrderInfoMapper;
import com.lyz.ServiceOrder.service.OrderInfoService;
import com.lyz.internalcommon.constant.OrderConstant;
import com.lyz.internalcommon.dto.OrderInfo;
import com.lyz.internalcommon.dto.ResponseResult;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
public class Mq {

//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue("payYes.queue"),
//            exchange = @Exchange(name = "pay.direct"),
//            key = "Yes"
//    ))
//    public void payRabbit(Long msg){
//        System.out.println("有msg了 我淦" + msg);
//        OrderInfo orderInfo = orderInfoMapper.selectById(msg);
//        orderInfo.setOrderStatus(OrderConstant.SUCCESS_PAY);
//        orderInfoMapper.updateById(orderInfo);
//    }
@RabbitListener(bindings = @QueueBinding(
        value = @Queue("payYes.queue"),
        exchange = @Exchange(name = "pay.fanout",type = "fanout")
//        key = "Yes"
))
public void payRabbit(Long msg){
    System.out.println("有msg了 我淦" + msg);
    OrderInfo orderInfo = orderInfoMapper.selectById(msg);
    orderInfo.setOrderStatus(OrderConstant.SUCCESS_PAY);
    orderInfoMapper.updateById(orderInfo);
}
// 1. 去修改订单表

//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = "payNo.queue"),
//            exchange = @Exchange(name = "pay.direct", type = ExchangeTypes.DIRECT),
//            key = "No"
//    ))
//    public void payNoRabbit(Long msg){
//        System.out.println("有msg了 我淦" + msg);
//        OrderInfo orderInfo = orderInfoMapper.selectById(msg);
//        orderInfo.setOrderStatus(6);
//        orderInfoMapper.updateById(orderInfo);
//    }

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
