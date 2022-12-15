package com.lyz.apiDriver.service;

import com.lyz.apiDriver.remote.ServiceOrderClient;
import com.lyz.internalcommon.constant.IdentityConstant;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.request.OrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderInfoService {
    @Autowired
    ServiceOrderClient serviceOrderClient;
    public ResponseResult toPickUpPassenger(OrderRequest orderRequest) {
        serviceOrderClient.toPickUpPassenger(orderRequest);
        return ResponseResult.success();
    }

    public ResponseResult arrivedDeparture(OrderRequest orderRequest) {
        serviceOrderClient.arrivedDeparture(orderRequest);
        return ResponseResult.success();
    }

    public ResponseResult pickUpPassenger(OrderRequest orderRequest) {
        serviceOrderClient.pickUpPassenger(orderRequest);
        return ResponseResult.success();
    }

    public ResponseResult passengerGetoff(OrderRequest orderRequest) {
        serviceOrderClient.passengerGetoff(orderRequest);
        return ResponseResult.success();
    }

    public ResponseResult cancel(Long orderId) {
        return serviceOrderClient.cancel(orderId, IdentityConstant.DRIVER_IDENTITY);
    }
}
