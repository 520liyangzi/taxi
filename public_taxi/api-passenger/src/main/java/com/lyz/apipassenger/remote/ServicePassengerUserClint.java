package com.lyz.apipassenger.remote;

import com.lyz.internalcommon.dto.PassengerUser;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.request.VerificationCodeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("service-passenger-user")
public interface ServicePassengerUserClint {
    @RequestMapping(method = RequestMethod.POST,value = "/user")
    ResponseResult loginOrRegister(@RequestBody VerificationCodeDTO verificationCodeDTO);

    @RequestMapping(method = RequestMethod.GET,value = "/user/{phone}")
    ResponseResult<PassengerUser> getUserByPhone(@PathVariable("phone")String phone);
    //用body传递的时候  method会变成post
}


