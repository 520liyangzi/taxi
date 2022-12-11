package com.lyz.apiDriver.controller;

import com.lyz.apiDriver.service.VerificationService;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.request.VerificationCodeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class VerificationController {

    @Autowired
    VerificationService verificationService;

    @GetMapping("/verification-code")
    public ResponseResult verificationCode(@RequestBody VerificationCodeDTO verificationCodeDTO){
        String driverPhone = verificationCodeDTO.getDriverPhone();
        log.info("司机号码"+driverPhone);
        return verificationService.checkAndSendVerification(driverPhone);
    }

    @PostMapping("/verfication-code-check")
    public ResponseResult checkVerificationCode(@RequestBody VerificationCodeDTO verificationCodeDTO){

        String driverPhone = verificationCodeDTO.getDriverPhone();
        String verificationCode = verificationCodeDTO.getVerificationCode();
        System.out.println("手机号"+driverPhone+"  验证码"+verificationCode);
        return verificationService.checkCode(driverPhone,verificationCode);
    }
}
