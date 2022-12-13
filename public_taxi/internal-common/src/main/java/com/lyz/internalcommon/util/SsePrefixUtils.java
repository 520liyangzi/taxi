package com.lyz.internalcommon.util;

public class SsePrefixUtils {
    public static final String sperator = "$";
    public static String gengeratorKey(Long userId,String identity){
        return userId + sperator + identity;
    }
}
