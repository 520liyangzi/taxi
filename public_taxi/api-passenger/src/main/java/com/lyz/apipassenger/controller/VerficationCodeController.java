package com.lyz.apipassenger.controller;


import com.lyz.apipassenger.service.VerficationCodeService;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.request.VerificationCodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class VerficationCodeController {

    @Autowired
    private VerficationCodeService verficationCodeService;

    @GetMapping("/verification-code")
    public ResponseResult verificationCode(@RequestBody VerificationCodeDTO verificationCodeDTO){
        String passengerPhone = verificationCodeDTO.getPassengerPhone();
        System.out.println(passengerPhone);
        return verficationCodeService.generatorCode(passengerPhone);
    }

    @PostMapping("/verfication-code-check")
    public ResponseResult checkVerificationCode(@RequestBody VerificationCodeDTO verificationCodeDTO){

        String passengerPhone = verificationCodeDTO.getPassengerPhone();
        String verificationCode = verificationCodeDTO.getVerificationCode();
        System.out.println("手机号"+passengerPhone+"  验证码"+verificationCode);
        return verficationCodeService.checkCode(passengerPhone,verificationCode);
    }

//    @GetMapping("/tt")
//    public String verificationCode(){
//        System.out.println("123");
//        return "111";
//    }
}
