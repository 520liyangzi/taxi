package com.lyz.messagesendservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class ttlConfig {

    @Bean
    public  DirectExchange getIt(){
        return new DirectExchange("ttl.direct");
    }

    @Bean
    public  Queue ttlQueue(){
        return QueueBuilder.durable("ttl.queue").ttl(10000).
        deadLetterRoutingKey("dl").deadLetterExchange("dl.direct").build();
    }

    @Bean
    public Binding bind(){
        return BindingBuilder.bind(ttlQueue()).to(getIt()).with("ttl");
    }
}
