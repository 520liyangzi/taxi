package com.lyz.apipassenger.service;

import com.lyz.apipassenger.remote.ServicePassengerUserClint;
import com.lyz.internalcommon.dto.PassengerUser;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.dto.TokenResult;
import com.lyz.internalcommon.request.VerificationCodeDTO;
import com.lyz.internalcommon.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    ServicePassengerUserClint servicePassengerUserClint;

    public ResponseResult getUserByAccessToken(String accessToken){
        //解析accesstoken  拿到手机号
        log.info("accessToken =  " + accessToken);

        TokenResult tokenResult = JwtUtils.checkToken(accessToken);
        String phone = tokenResult.getPhone();
        log.info("手机号 :  " + phone);

//        VerificationCodeDTO verificationCodeDTO = new VerificationCodeDTO();
//        verificationCodeDTO.setPassengerPhone(phone);

        ResponseResult<PassengerUser> userByPhone = servicePassengerUserClint.getUserByPhone(phone);


        //根据手机号查询用户信息
//        PassengerUser passengerUser = new PassengerUser();
//        passengerUser.setPassengerName("张三");
//        passengerUser.setProfilePhoto("头像");
        //响应信息

        return ResponseResult.success(userByPhone.getData());
    }
}
