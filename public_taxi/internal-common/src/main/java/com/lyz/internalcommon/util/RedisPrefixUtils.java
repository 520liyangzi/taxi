package com.lyz.internalcommon.util;

public class RedisPrefixUtils {

    public static final String verificationCodePrefix = "passenger-verification-code-";

    public static final String  tkoenPrefix = "token-";

    //黑名单设备号
    public static String blackDeviceCodePrefix = "black-device-";

    public static String generatorKeyByPhone(String Phone,String identity){
        return verificationCodePrefix +identity+"-"+ Phone;
    }

    public static String generatorTokenKey(String phone,String identity,String tokenType){
        return tkoenPrefix + phone + "-" + identity +"-"+tokenType;
    }
}
