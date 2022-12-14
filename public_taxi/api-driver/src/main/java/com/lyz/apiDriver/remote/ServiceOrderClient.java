package com.lyz.apiDriver.remote;

import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.request.OrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("service-order")
public interface ServiceOrderClient {

    @RequestMapping(method = RequestMethod.POST,value = "/order/to-pick-up-passenger")
    public ResponseResult toPickUpPassenger(@RequestBody OrderRequest orderRequest);

    @RequestMapping(method = RequestMethod.GET,value = "/order/arrived-departure")
    ResponseResult arrivedDeparture(OrderRequest orderRequest);

    @RequestMapping(method = RequestMethod.GET,value = "/order/pick-up-passenger")
    ResponseResult pickUpPassenger(OrderRequest orderRequest);

    @RequestMapping(method = RequestMethod.GET,value = "/order/passenger-getoff")
    ResponseResult passengerGetoff(OrderRequest orderRequest);
}
