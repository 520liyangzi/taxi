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

    /**
     * 接乘客
     * @param orderRequest
     * @return
     */
    @PostMapping("/to-pick-up-passenger")
    public ResponseResult changeStatus(@RequestBody OrderRequest orderRequest){

        return orderInfoService.toPickUpPassenger(orderRequest);
    }

    /**
     * 到达乘客目的地
     * @param orderRequest
     * @return
     */
    @PostMapping("/arrived-departure")
    public ResponseResult arrivedDeparture(@RequestBody OrderRequest orderRequest){

        return orderInfoService.arrivedDeparture(orderRequest);
    }


    /**
     * 司机接到乘客
     * @param orderRequest
     * @return
     */
    @PostMapping("/pick-up-passenger")
    public ResponseResult pickUpPassenger(@RequestBody OrderRequest orderRequest){
        //pick_up_passenger_time
        return orderInfoService.pickUpPassenger(orderRequest);
    }


    /**
     * 乘客到达目的地---行程终止
     * @param orderRequest
     * @return
     */
    @PostMapping("/passenger-getoff")
    public ResponseResult passengerGetoff(@RequestBody OrderRequest orderRequest){
        //pick_up_passenger_time
        return orderInfoService.passengerGetoff(orderRequest);
    }

    /**
     * 支付完成
     * @param orderRequest
     * @return
     */
    @PostMapping("/pay")
    public ResponseResult pay(@RequestBody OrderRequest orderRequest){
        return orderInfoService.pay(orderRequest);
    }

    /**
     * 订单取消
     * @param orderId
     * @param identity
     * @return
     */
    @PostMapping("/cancel")
    public ResponseResult cancel(Long orderId,String identity){

        return orderInfoService.cancel(orderId,identity);
    }

    @PostMapping("/push-pay-info")
    public ResponseResult pushPayInfo(@RequestBody OrderRequest orderRequest){
        return orderInfoService.pushPayInfo(orderRequest);
    }

}
