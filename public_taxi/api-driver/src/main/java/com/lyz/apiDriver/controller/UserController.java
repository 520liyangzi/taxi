package com.lyz.apiDriver.controller;

import com.lyz.apiDriver.service.UserService;
import com.lyz.internalcommon.dto.DriverUser;
import com.lyz.internalcommon.dto.DriverUserWorkStatus;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.dto.TokenResult;
import com.lyz.internalcommon.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/user")
    public ResponseResult updateUser(@RequestBody DriverUser driverUser){
        return  userService.updateUser(driverUser);
    }

    @PostMapping("/driver-user-work-status")
    public ResponseResult changeWorkStatus(@RequestBody DriverUserWorkStatus driverUserWorkStatus){
        return userService.changeWorkStatus(driverUserWorkStatus);
    }

    @GetMapping("/driver-car- binding-relationship")
    public ResponseResult getCarBindingByDriverId(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        TokenResult tokenResult = JwtUtils.checkToken(authorization);
        String driverPhone = tokenResult.getPhone();
        return userService.getCarBindingByDriverId(driverPhone);
    }
}
