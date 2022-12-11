package com.lyz.ServiceOrder.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyz.ServiceOrder.service.OrderInfoService;
import com.lyz.internalcommon.constant.HeaderParamConstant;
import com.lyz.internalcommon.constant.OrderConstant;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.request.OrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    OrderInfoService orderInfoService;

    @PostMapping("/add")
    public ResponseResult add(@RequestBody OrderRequest orderRequest,HttpServletRequest httpServletRequest){
//        String deviceCode = httpServletRequest.getHeader(HeaderParamConstant.DEVICE_CODE);
//        orderRequest.setDeviceCode(deviceCode);
        System.out.println("add了");


        return orderInfoService.add(orderRequest);
    }

}
