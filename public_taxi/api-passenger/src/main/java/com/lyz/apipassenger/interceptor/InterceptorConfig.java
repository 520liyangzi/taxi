package com.lyz.apipassenger.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    //在拦截器初始化的时候   在这里直接搞一个bean
    @Bean
    public JwtInterceptor jwtInterceptor(){
        return new JwtInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
        //拦截的路径
                .addPathPatterns("/**")

        //不拦截的路径
                .excludePathPatterns("/noAuthTest")
                .excludePathPatterns("/verification-code").
                excludePathPatterns("/verfication-code-check")
                .excludePathPatterns("/token-refresh")
                .excludePathPatterns("/test-real-time-order/**");
    }
}
