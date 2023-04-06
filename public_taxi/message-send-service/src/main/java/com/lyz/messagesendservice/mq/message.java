package com.lyz.messagesendservice.mq;

import com.lyz.messagesendservice.client.SseEmitterClient;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
public class message {

    @Autowired
    SseEmitterClient sseEmitterClient;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("sendMessageYes.queue"),
            exchange = @Exchange(name = "pay.fanout",type = "fanout")
    ))
    public void message(Long msg){
    System.out.println("来了");
        sseEmitterClient.push(1631106614845407234L,"0","mq这里说支付成功了");
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("ttlShou.queue"),
            exchange = @Exchange(name = "dl.direct"),
            key = "dl"
    ))
    public void shou(String msg){
    System.out.println(msg);
    System.out.println("给钱！！！");
    }

}
