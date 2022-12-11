package com.lyz.ServiceMap.controller;


import com.lyz.ServiceMap.service.ServiceMapService;
import com.lyz.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service")
public class ServiceController {


    @Autowired
    ServiceMapService serviceMapService;

    @PostMapping("/add")
    public ResponseResult add(String name){
        return serviceMapService.add(name);
    }


}
