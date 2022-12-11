package com.lyz.apipassenger.controller;

import com.lyz.apipassenger.service.ForecastPriceService;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.request.ForecastPriceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ForecatPriceController {

    @Autowired
    ForecastPriceService forecastPriceService;

    @PostMapping("/forecast-price")
    public ResponseResult forecastPrice(@RequestBody ForecastPriceDTO forecastPriceDTO){
        String depLongitude = forecastPriceDTO.getDepLongitude();
        String depLatitude = forecastPriceDTO.getDepLatitude();
        String destLongitude = forecastPriceDTO.getDestLongitude();
        String destLatitude = forecastPriceDTO.getDestLatitude();
        String cityCode = forecastPriceDTO.getCityCode();
        String vehicleType = forecastPriceDTO.getVehicleType();
        log.info("出发地经度 :  " + forecastPriceDTO.getDepLongitude());
        log.info("出发地维度 :  " + forecastPriceDTO.getDepLatitude());
        log.info("目的地经度:  " + forecastPriceDTO.getDestLongitude());
        log.info("目的地维度 :  " + forecastPriceDTO.getDestLatitude());
        return forecastPriceService.forecastPrice(depLongitude, depLatitude, destLongitude, destLatitude,cityCode,vehicleType);
    }
}
