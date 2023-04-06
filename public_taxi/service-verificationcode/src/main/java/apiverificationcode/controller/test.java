package apiverificationcode.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
public class test {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/ttt")
    public void testSendMessage2SimpleQueue() throws InterruptedException {
        // 1.准备消息
        String message = "hello, spring amqpppppppp!";
        // 2.准备CorrelationData
        // 2.1.消息ID
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        // 2.2.准备ConfirmCallback   失败的话  都没有到交换机
        correlationData.getFuture().addCallback(result -> {
            // 判断结果
            if (result.isAck()) {
                // ACK
                log.debug("消息成功投递到交换机！消息ID: {}", correlationData.getId());
            } else {
                // NACK
                log.error("消息投递到交换机失败！消息ID：{}", correlationData.getId());
                // 重发消息
            }
        }, ex -> {
            // 记录日志   抛出了异常  回调都没收到
            log.error("消息发送失败！", ex);
            // 重发消息
        });
        // 3.发送消息
        rabbitTemplate.convertAndSend("amq.topic", "simple.test", message,correlationData);
    }
}
