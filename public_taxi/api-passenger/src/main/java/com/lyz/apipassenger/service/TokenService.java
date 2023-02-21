package com.lyz.apipassenger.service;


import com.lyz.internalcommon.constant.CommonStatusEnum;
import com.lyz.internalcommon.constant.TokenConstants;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.dto.TokenResult;
import com.lyz.internalcommon.response.TokenResponse;
import com.lyz.internalcommon.util.JwtUtils;
import com.lyz.internalcommon.util.RedisPrefixUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public ResponseResult refreshToken(String refreshTokenSrc){
        //解析 refresh  是否合法
        TokenResult tokenResult = JwtUtils.checkToken(refreshTokenSrc);
        if(tokenResult == null){
            return ResponseResult.fail(CommonStatusEnum.TOKEN_ERROR.getCode(),CommonStatusEnum.TOKEN_ERROR.getValue());
        }
        String phone = tokenResult.getPhone();
        String identidy = tokenResult.getIdentity();

        //去读取redis中的refreshtoken
        String refreshTokenKey = RedisPrefixUtils.generatorTokenKey(phone, identidy, TokenConstants.REFRESH_TOKEN_TYOE);
        String refreshTokenRedis = stringRedisTemplate.opsForValue().get(refreshTokenKey);

        //校验token
        if((StringUtils.isBlank(refreshTokenRedis)) || (!refreshTokenSrc.trim().equals(refreshTokenRedis.trim()))){
            return ResponseResult.fail(CommonStatusEnum.TOKEN_ERROR.getCode(),CommonStatusEnum.TOKEN_ERROR.getValue());
        }

        String refreshToken =  JwtUtils.generatorToken(phone,identidy,TokenConstants.REFRESH_TOKEN_TYOE);
        String accessToken =  JwtUtils.generatorToken(phone,identidy,TokenConstants.ACCESS_TOKEN_TYPE);

        String accessTokenKey = RedisPrefixUtils.generatorTokenKey(phone,identidy,TokenConstants.ACCESS_TOKEN_TYPE);

        stringRedisTemplate.opsForValue().set(accessTokenKey,accessToken,30, TimeUnit.DAYS);
        stringRedisTemplate.opsForValue().set(refreshTokenKey,refreshToken,31,TimeUnit.DAYS);

//        stringRedisTemplate.opsForValue().set(accessTokenKey,accessToken,10, TimeUnit.SECONDS);
//        stringRedisTemplate.opsForValue().set(refreshTokenKey,refreshToken,100,TimeUnit.SECONDS);
        //生成双token

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setRefrshToken(refreshToken);
        tokenResponse.setAccessToken(accessToken);
        return ResponseResult.success(tokenResponse);
    }
}
