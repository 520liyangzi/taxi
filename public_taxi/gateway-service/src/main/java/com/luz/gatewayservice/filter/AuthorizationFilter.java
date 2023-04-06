package com.luz.gatewayservice.filter;

import com.lyz.internalcommon.constant.TokenConstants;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.dto.TokenResult;
import com.lyz.internalcommon.util.JwtUtils;
import com.lyz.internalcommon.util.RedisPrefixUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationFilter implements GlobalFilter, Ordered {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        if (request.getURI().getPath().contains("verification-code") ||
                request.getURI().getPath().contains("verfication-code-check")||
                request.getURI().getPath().contains("Authorization-refresh")){
            return chain.filter(exchange);
        }
        boolean result = true;
        String resultString = "";
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst("Authorization");
        TokenResult tokenResult = JwtUtils.checkToken(token);
        if(tokenResult == null){
            resultString = "token error";
      System.out.println(resultString);
            result = false;
        }else {
            String phone = tokenResult.getPhone();
            String identity = tokenResult.getIdentity();
            String tokenKey = RedisPrefixUtils.generatorTokenKey(phone,identity, TokenConstants.ACCESS_TOKEN_TYPE);
            String tokenRedis = stringRedisTemplate.opsForValue().get(tokenKey);
            if(StringUtils.isBlank(tokenRedis)){
                resultString = "access token invalid";
                System.out.println(resultString);
                result = false;
            }else {
                if(tokenRedis.trim().equals(tokenKey.trim())){
                    resultString = "access token is not same";
                    System.out.println(resultString);
                    result = false;
                }
            }
        }
        if(!result){
            //失败信息
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        //合法 放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
