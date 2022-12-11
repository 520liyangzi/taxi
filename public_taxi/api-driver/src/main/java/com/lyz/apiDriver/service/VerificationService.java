package com.lyz.apiDriver.service;

import com.lyz.apiDriver.remote.ServiceDriverUserClient;
import com.lyz.apiDriver.remote.ServiceVerificationCodeClient;
import com.lyz.internalcommon.constant.CommonStatusEnum;
import com.lyz.internalcommon.constant.IdentityConstant;
import com.lyz.internalcommon.constant.TokenConstants;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.request.VerificationCodeDTO;
import com.lyz.internalcommon.response.DriverUserExistsResponse;
import com.lyz.internalcommon.response.NumberCodeResponse;
import com.lyz.internalcommon.response.TokenResponse;
import com.lyz.internalcommon.util.JwtUtils;
import com.lyz.internalcommon.util.RedisPrefixUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class VerificationService {

    @Autowired
    ServiceDriverUserClient serviceDriverUserClient;

    @Autowired
    ServiceVerificationCodeClient serviceVerificationCodeClient;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public ResponseResult  checkAndSendVerification(String driverPhone){
        //查询driverUser  该手机号的司机是否存在
        ResponseResult<DriverUserExistsResponse> driverUserExistsResponseResponseResult = serviceDriverUserClient.checkDriver(driverPhone);
        DriverUserExistsResponse data = driverUserExistsResponseResponseResult.getData();
        int isExists = data.getIsExists();
        if(isExists != 1){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_NOT_EXISTS.getCode(),CommonStatusEnum.DRIVER_NOT_EXISTS.getValue());
        }
        String driverPhone1 = data.getDriverPhone();
        log.info(driverPhone + "存在");


        //获取验证码
        ResponseResult<NumberCodeResponse> numberCode = serviceVerificationCodeClient.getNumberCode(6);
        NumberCodeResponse numberCodeResponse = numberCode.getData();
        int numberCode1 = numberCodeResponse.getNumberCode();
        log.info("验证码  "+ numberCode1);

        //调用第三方发送验证码
        String key = RedisPrefixUtils.generatorKeyByPhone(driverPhone, IdentityConstant.DRIVER_IDENTITY);
        //IdentityConstant.DRIVER_IDENTITY is 0
        stringRedisTemplate.opsForValue().set(key,numberCode1+"",2, TimeUnit.MINUTES);

        //存入redis
        return ResponseResult.success("");
    }

    public ResponseResult checkCode(String driverPhone, String verificationCode){
        //根据手机号去redis读取验证码！！！
        String key = RedisPrefixUtils.generatorKeyByPhone(driverPhone,IdentityConstant.DRIVER_IDENTITY);
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

        //颁发令牌!!!   md5可以用暴力查询的方式查到   jwt有个secrety 就很安全  别人破解不出来

        String accessToken = JwtUtils.generatorToken(driverPhone, IdentityConstant.DRIVER_IDENTITY, TokenConstants.ACCESS_TOKEN_TYPE);
        String refrshToken = JwtUtils.generatorToken(driverPhone, IdentityConstant.DRIVER_IDENTITY,TokenConstants.REFRESH_TOKEN_TYOE);

        String accessTokenKey = RedisPrefixUtils.generatorTokenKey(driverPhone,IdentityConstant.DRIVER_IDENTITY,TokenConstants.ACCESS_TOKEN_TYPE);
        stringRedisTemplate.opsForValue().set(accessTokenKey,accessToken,30,TimeUnit.DAYS);
        // stringRedisTemplate.opsForValue().set(accessTokenKey,accessToken,10,TimeUnit.SECONDS);

        String refreshTokenKey = RedisPrefixUtils.generatorTokenKey(driverPhone,IdentityConstant.DRIVER_IDENTITY,TokenConstants.REFRESH_TOKEN_TYOE);
        stringRedisTemplate.opsForValue().set(refreshTokenKey,accessToken,35,TimeUnit.DAYS);
        //   stringRedisTemplate.opsForValue().set(refreshTokenKey,accessToken,100,TimeUnit.SECONDS);
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setRefrshToken(refrshToken);

        return ResponseResult.success(tokenResponse);
    }
}
