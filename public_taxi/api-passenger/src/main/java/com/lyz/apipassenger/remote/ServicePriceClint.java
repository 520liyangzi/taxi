package com.lyz.apipassenger.remote;


import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.request.ForecastPriceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("service-price")
public interface ServicePriceClint {

    @RequestMapping(method = RequestMethod.POST,value = ("/forecast-price"))
    public ResponseResult forecastPrice(@RequestBody ForecastPriceDTO forecastPriceDTO);
}
