package com.lyz.ServiceMap.controller;


import com.lyz.ServiceMap.service.DistrictService;
import com.lyz.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DistrictController {

    @Autowired
    DistrictService districtService;

    @GetMapping("/dic-district")
    public ResponseResult initDistrict(String keywords){

        return districtService.initDicDistrict(keywords);
    }
}
