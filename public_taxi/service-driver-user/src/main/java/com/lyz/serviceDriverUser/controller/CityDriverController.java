package com.lyz.serviceDriverUser.controller;

import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.serviceDriverUser.service.CityDriverUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/city-driver")
public class CityDriverController {

    @Autowired
    CityDriverUserService cityDriverUserService;

    @GetMapping("/is-available-driver")
    public ResponseResult isAvailibleDriver(@RequestParam("cityCode") String cityCode){
        System.out.println("有没有啊");
        System.out.println("city-driver/is-available-driver");
        return cityDriverUserService.isAvailibleDriver(cityCode);
    }

}
