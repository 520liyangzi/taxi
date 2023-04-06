package testalipay.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class a {
    // https://6i587977f2.zicp.fun/alipay/notify
    @Resource
    RabbitTemplate rabbitTemplate;

    @GetMapping("/test")
    public String t(){
        String ii = "1584370883330600969";
        rabbitTemplate.convertAndSend("pay.queue",ii);
        return "穿透";
    }
}
