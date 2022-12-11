package com.lyz.apipassenger.service;

import com.lyz.apipassenger.remote.ServicePriceClint;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.request.ForecastPriceDTO;
import com.lyz.internalcommon.response.ForecastPriceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Service
public class ForecastPriceService {
    @Autowired
    ServicePriceClint servicePriceClint;

    public ResponseResult forecastPrice(String depLongitude,String depLatitude,String destLongitude,String destLatitude,
                                        String cityCode,String vehicleType){

        log.info("开始几家");
        log.info("出发地经度 :  " +depLongitude);
        log.info("出发地维度 :  " + depLatitude);
        log.info("目的地经度:  " + destLongitude);
        log.info("目的地维度 :  " + destLatitude);
        log.info("机甲结果");
        ForecastPriceDTO forecastPriceDTO = new ForecastPriceDTO();
        forecastPriceDTO.setDepLongitude(depLongitude);
        forecastPriceDTO.setDepLatitude(depLatitude);
        forecastPriceDTO.setDestLongitude(destLongitude);
        forecastPriceDTO.setDestLatitude(destLatitude);
        forecastPriceDTO.setCityCode(cityCode);
        forecastPriceDTO.setVehicleType(vehicleType);

        ResponseResult responseResult = servicePriceClint.forecastPrice(forecastPriceDTO);

        return ResponseResult.success(responseResult);
    }
}
