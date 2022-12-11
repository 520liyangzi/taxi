package com.lyz.servicepassengeruser.controller;


import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.request.VerificationCodeDTO;
import com.lyz.servicepassengeruser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/user")
    public ResponseResult loginOrRegister(@RequestBody VerificationCodeDTO verificationCodeDTO){
        String passengerPhone = verificationCodeDTO.getPassengerPhone();
        System.out.println("phone is "+passengerPhone);
        return userService.loginOrRegister(passengerPhone);
    }

    @GetMapping("user/{phone}")
    public ResponseResult getUser(@PathVariable("phone")String passengerPhone){
        System.out.println("phone is "+passengerPhone);
        return userService.getUserByPhone(passengerPhone);
    }
}
