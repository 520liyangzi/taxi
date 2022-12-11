package com.lyz.apipassenger.service;


import com.lyz.apipassenger.remote.ServicePassengerUserClint;
import com.lyz.apipassenger.remote.ServiceVerificationCodeClint;
import com.lyz.internalcommon.constant.CommonStatusEnum;
import com.lyz.internalcommon.constant.IdentityConstant;
import com.lyz.internalcommon.constant.TokenConstants;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.request.VerificationCodeDTO;
import com.lyz.internalcommon.response.NumberCodeResponse;
import com.lyz.internalcommon.response.TokenResponse;
import com.lyz.internalcommon.util.JwtUtils;
import com.lyz.internalcommon.util.RedisPrefixUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class VerficationCodeService {

    @Autowired
    ServiceVerificationCodeClint serviceVerificationCodeClint;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ServicePassengerUserClint servicePassengerUserClint;



    public ResponseResult generatorCode(String passengerPhone){
        //调用验证码服务获取

        ResponseResult<NumberCodeResponse> numberCodeResponse = serviceVerificationCodeClint.getNumberCode(6);
        //调用远程服务获取验证码！！！
        int numberCode = numberCodeResponse.getData().getNumberCode();
        //key value 和过期时间
        String key = RedisPrefixUtils.generatorKeyByPhone(passengerPhone,IdentityConstant.PASSENGER_IDENTITY);
        //存入
        stringRedisTemplate.opsForValue().set(key,numberCode+"",2, TimeUnit.MINUTES);

        return ResponseResult.success("");
    }



    public ResponseResult checkCode(String passengerPhone, String verificationCode){
        //根据手机号去redis读取验证码！！！
        String key = RedisPrefixUtils.generatorKeyByPhone(passengerPhone,IdentityConstant.PASSENGER_IDENTITY);
        String codeRedis = stringRedisTemplate.opsForValue().get(key);
        //校验！！！
        if(StringUtils.isBlank(codeRedis)){
            return ResponseResult.fail(CommonStatusEnum.VERIFICATION_CODE_ERROR.getCode(),CommonStatusEnum.VERIFICATION_CODE_ERROR.getValue());
        }

        if(!verificationCode.trim().equals(codeRedis.trim())){
            return ResponseResult.fail(CommonStatusEnum.VERIFICATION_CODE_ERROR.getCode(),CommonStatusEnum.VERIFICATION_CODE_ERROR.getValue());
        }

        //判断以前是否有用户并对应进行处理!!!

        //这里进行远程服务调用-------service passenger user那块
        System.out.println("判断以前是否有用户并对应进行处理!!!");

        VerificationCodeDTO verificationCodeDTO = new VerificationCodeDTO();
        verificationCodeDTO.setPassengerPhone(passengerPhone);
        servicePassengerUserClint.loginOrRegister(verificationCodeDTO);

        //颁发令牌!!!   md5可以用暴力查询的方式查到   jwt有个secrety 就很安全  别人破解不出来
        System.out.println("颁发令牌!!!");

        String accessToken = JwtUtils.generatorToken(passengerPhone, IdentityConstant.PASSENGER_IDENTITY, TokenConstants.ACCESS_TOKEN_TYPE);
        String refrshToken = JwtUtils.generatorToken(passengerPhone, IdentityConstant.PASSENGER_IDENTITY,TokenConstants.REFRESH_TOKEN_TYOE);

        String accessTokenKey = RedisPrefixUtils.generatorTokenKey(passengerPhone,IdentityConstant.PASSENGER_IDENTITY,TokenConstants.ACCESS_TOKEN_TYPE);
        stringRedisTemplate.opsForValue().set(accessTokenKey,accessToken,30,TimeUnit.DAYS);
       // stringRedisTemplate.opsForValue().set(accessTokenKey,accessToken,10,TimeUnit.SECONDS);

        String refreshTokenKey = RedisPrefixUtils.generatorTokenKey(passengerPhone,IdentityConstant.PASSENGER_IDENTITY,TokenConstants.REFRESH_TOKEN_TYOE);
        stringRedisTemplate.opsForValue().set(refreshTokenKey,accessToken,35,TimeUnit.DAYS);
     //   stringRedisTemplate.opsForValue().set(refreshTokenKey,accessToken,100,TimeUnit.SECONDS);
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setRefrshToken(refrshToken);

        return ResponseResult.success(tokenResponse);
    }
}
