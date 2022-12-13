package com.lyz.ServiceOrder.remote;

import com.lyz.internalcommon.dto.Car;
import com.lyz.internalcommon.dto.PriceRule;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.response.OrderDriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("service-driver-user")
public interface ServiceDriverUserClient {
    @RequestMapping(method = RequestMethod.GET,value = "city-driver/is-available-driver")
    public ResponseResult<Boolean> isAvailableDriver(@RequestParam String cityCode);


    @RequestMapping(method = RequestMethod.GET,value = "/get-availiable-driver/{carId}")
    public ResponseResult<OrderDriverResponse> getAvailiableDriver(@PathVariable("carId") Long carId);

    @RequestMapping(method = RequestMethod.GET,value = "/car")
    ResponseResult<Car> getCarById(@RequestParam Long carId);
}
