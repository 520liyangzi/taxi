package com.lyz.apiBoss.service;

import com.lyz.apiBoss.remote.ServiceDriverUserClient;
import com.lyz.internalcommon.dto.Car;
import com.lyz.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarService {

    @Autowired
    ServiceDriverUserClient serviceDriverUserClient;

    public ResponseResult car(Car car){
        serviceDriverUserClient.addCar(car);
        return ResponseResult.success();
    }
}
