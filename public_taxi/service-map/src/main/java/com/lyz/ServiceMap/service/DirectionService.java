package com.lyz.ServiceMap.service;

import com.lyz.ServiceMap.remote.MapDirectionClient;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.request.ForecastPriceDTO;
import com.lyz.internalcommon.response.DirectionResponse;
import com.lyz.internalcommon.response.ForecastPriceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DirectionService {

    @Autowired
    MapDirectionClient mapDirectionClient;

    public ResponseResult driving(String depLongitude,String depLatitude,String destLongitude,String destLatitude){
        //调用第三方地图接口哦～～～

        DirectionResponse direction = mapDirectionClient.direction(depLongitude, depLatitude, destLongitude, destLatitude);
        return ResponseResult.success(direction);
    }
}
