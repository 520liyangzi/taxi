package com.lyz.serviceDriverUser.service;

import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.serviceDriverUser.mapper.DriverUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityDriverUserService {
    @Autowired
    DriverUserMapper driverUserMapper;

    public ResponseResult<Boolean> isAvailibleDriver(String cityCode){
        int i = driverUserMapper.selectDriverUserCountByCityCode(cityCode);
        if(i > 0){
            return ResponseResult.success(true);
        }else {
            return ResponseResult.success(false);
        }
    }
}
