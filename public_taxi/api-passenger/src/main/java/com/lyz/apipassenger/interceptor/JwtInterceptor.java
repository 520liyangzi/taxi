package com.lyz.apipassenger.interceptor;
import com.lyz.internalcommon.constant.TokenConstants;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.dto.TokenResult;
import com.lyz.internalcommon.util.JwtUtils;
import com.lyz.internalcommon.util.RedisPrefixUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean result = true;
        String resultString = "";
        String token = request.getHeader("Authorization");
        TokenResult tokenResult = JwtUtils.checkToken(token);
        //从 redis中取出token   看看是否一样！
        if(tokenResult == null){
            resultString = "token error";
            result = false;
        }else {
            String phone = tokenResult.getPhone();
            String identity = tokenResult.getIdentity();
            String tokenKey = RedisPrefixUtils.generatorTokenKey(phone,identity, TokenConstants.ACCESS_TOKEN_TYPE);
            String tokenRedis = stringRedisTemplate.opsForValue().get(tokenKey);
            if(StringUtils.isBlank(tokenRedis)){
                resultString = "access token invalid";
                result = false;
            }else {
                if(!tokenRedis.trim().equals(token.trim())){
                    resultString = "access token is not same";
                    result = false;
                }
            }
        }
        if(!result){
            PrintWriter out = response.getWriter();
            out.print(JSONObject.fromObject(ResponseResult.fail(resultString)).toString());
            //等于这样就直接返回了那个 JSON对象
        }
        return result;
    }
}
