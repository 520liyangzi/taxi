package com.lyz.internalcommon.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lyz.internalcommon.dto.TokenResult;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    private static final String SIGN = "LYZlyz@123";

    private static final String JWT_KEY_PHONE = "passengerPhone";

    private static final String JWT_KEY_IDENTITY = "identity";

    private static final String JWT_TOKEN_TYPE = "tokenType";

    private static final String JWT_TOKEN_TIME = "tokenTime";

    //生成token
    public static String generatorToken(String passengerPhone,String identity,String tokenType){
        //token过期时间
        Map<String,String> map = new HashMap<>();
        map.put(JWT_KEY_PHONE,passengerPhone);
        map.put(JWT_KEY_IDENTITY,identity);
        map.put(JWT_TOKEN_TYPE,tokenType);
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        map.put(JWT_TOKEN_TIME,date.toString());
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DATE,1);
//        Date date = calendar.getTime();

        JWTCreator.Builder builder = JWT.create();
        map.forEach(
                (k, v)->{
                    //map的k  v 逐个迭代加入到builder里
            builder.withClaim(k,v);
        });
        //整合过期时间
//        builder.withExpiresAt(date);

        //生成
        String sign = builder.sign(Algorithm.HMAC256(SIGN));

        return sign;
    }



    //解析token

    public static TokenResult passToken(String token){
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
        String phone = verify.getClaim(JWT_KEY_PHONE).asString();
        String identity = verify.getClaim(JWT_KEY_IDENTITY).asString();

        TokenResult tokenResult = new TokenResult();
        tokenResult.setPhone(phone);
        tokenResult.setIdentity(identity);
        return tokenResult;
    }


    public static TokenResult checkToken(String token){
        TokenResult tokenResult = null;
        try {
            //解析出来的就是token结果
            tokenResult =  JwtUtils.passToken(token);
            return tokenResult;
        }catch (Exception e){

        }
        return null;
    }
}
