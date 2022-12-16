package com.lyz.apiDriver.remote;

import com.lyz.internalcommon.dto.*;
import com.lyz.internalcommon.response.DriverUserExistsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("service-driver-user")
public interface ServiceDriverUserClient {
    @RequestMapping(method = RequestMethod.PUT,value = "/user")
    ResponseResult updateUser(@RequestBody DriverUser driverUser);

    @RequestMapping(method = RequestMethod.GET,value = "/check-driver/{driverPhone}")
    ResponseResult<DriverUserExistsResponse> checkDriver(@PathVariable("driverPhone") String driverPhone);

    @RequestMapping(method = RequestMethod.GET,value = "/car")
    ResponseResult<Car> getCarbyId(@RequestParam Long carId);

    @RequestMapping(method = RequestMethod.POST,value = "/driver-user-work-status")
    ResponseResult changeWorkStatus(@RequestBody DriverUserWorkStatus driverUserWorkStatus);

    @GetMapping("/driver-car-binding-relationship")
    ResponseResult<DriverCarBindingRelationship> getCarBindingByDriverId(@RequestParam String driverPhone);
}
