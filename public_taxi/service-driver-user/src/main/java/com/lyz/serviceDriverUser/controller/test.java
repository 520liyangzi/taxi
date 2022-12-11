package com.lyz.serviceDriverUser.controller;

import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.serviceDriverUser.mapper.DriverCarBindingRelationshipMapper;
import com.lyz.serviceDriverUser.mapper.DriverUserMapper;
import com.lyz.serviceDriverUser.service.DriverUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {
    @Autowired
    DriverUserService driverUserService;

    @Autowired
    DriverUserMapper driverUserMapper;

    @GetMapping("/test")
    public ResponseResult test(){
        return driverUserService.testGerDriverUser();
    }

    @GetMapping("/test-xml")
    public int testXml(String cityCode){
        return driverUserMapper.selectDriverUserCountByCityCode(cityCode);
    }
}
