package com.lyz.serviceDriverUser.controller;


import com.lyz.internalcommon.dto.Car;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.serviceDriverUser.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Yangzi
 * @since 2022-12-01
 */
@RestController
public class CarController {
    @Autowired
    CarService carService;

    @PostMapping("/car")
    public ResponseResult addCar(@RequestBody Car car){
        LocalDateTime now = LocalDateTime.now();
        car.setGmtCreate(now);
        car.setGmtModified(now);
        return carService.addCar(car);
    }

    @GetMapping("/car")
    public ResponseResult getCarbyId(Long carId){
        return carService.getCarbyId(carId);
    }

}
