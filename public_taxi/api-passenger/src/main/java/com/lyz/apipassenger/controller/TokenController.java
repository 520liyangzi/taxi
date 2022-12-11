package com.lyz.apipassenger.controller;


import com.lyz.apipassenger.service.TokenService;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.response.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    TokenService tokenService;

    @PostMapping("/token-refresh")
    public ResponseResult refreshToken(@RequestBody TokenResponse tokenResponse){
        String refreshTokenSrc = tokenResponse.getRefrshToken();
        System.out.println("===refreshToken这个执行了   这是原来的");
        return tokenService.refreshToken(refreshTokenSrc);
    }
}
