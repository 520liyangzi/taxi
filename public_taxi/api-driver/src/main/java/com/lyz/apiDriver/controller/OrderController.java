package com.lyz.apiDriver.controller;

import com.lyz.apiDriver.service.OrderInfoService;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderInfoService orderInfoService;

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


}
