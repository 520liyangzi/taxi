package com.lyz.ServiceMap.controller;

import com.lyz.ServiceMap.mapper.DicDistrictMapper;
import com.lyz.internalcommon.dto.DicDistrict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test(){
        return "service map";
    }

    @Autowired
    DicDistrictMapper dicDistrictMapper;

    @GetMapping("/ttt")
    public String testMapper(){
        Map<String,Object> map = new HashMap<>();
        map.put("address_code","111");
        List<DicDistrict> dicDistricts = dicDistrictMapper.selectByMap(map);
        System.out.println(dicDistricts);
        return "123";
    }
}
