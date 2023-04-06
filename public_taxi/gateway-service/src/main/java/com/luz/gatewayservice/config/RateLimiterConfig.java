package com.luz.gatewayservice.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Configuration
public class RateLimiterConfig {
//    @Bean
//    public KeyResolver ipKeyResolver() {
//        return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getQueryParams().getFirst("key")));
//    }

    @Bean
    public KeyResolver ipKeyResolver(){
//        return new KeyResolver() {
//            @Override
//            public Mono<String> resolve(ServerWebExchange exchange) {
//                return Mono.justOrEmpty(exchange.getRequest().getRemoteAddress().getHostName());
//            }
//        };
        return exchange -> Mono.just("global_key");
    }
//    @Bean
//    public KeyResolver tokenKeyResolver() {
//        return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getHeaders().getFirst("token")));
//    }

}


