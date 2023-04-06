package apiverificationcode.controller;


import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.response.NumberCodeResponse;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
public class numberCodeController {

    @Autowired
    RabbitTemplate rabbitTemplate;


    @GetMapping("/testYes")
    public String t(){
    System.out.println("!234565432");
        Long ii = 1584370883330600969L;
        rabbitTemplate.convertAndSend("pay.fanout","",ii);
    System.out.println("234565432");
        return "穿透";
    }

    @GetMapping("/shou")
    public String yy(){
        System.out.println("!shoukuan");
        Message message = MessageBuilder.withBody("money".getBytes(StandardCharsets.UTF_8))
                        .setExpiration("10000").build();
        CorrelationData correlationData = new CorrelationData("12312");
        rabbitTemplate.convertAndSend("ttl.direct","ttl","给钱啊求你了");
        System.out.println("234565432");
        return "穿透";
    }


    @GetMapping("/testNo")
    public String tt(){
        System.out.println("!234565432");
        Long ii = 1584370883330600969L;
        rabbitTemplate.convertAndSend("pay.direct","No",ii);
        System.out.println("234565432");
        return "穿透";
    }


    @GetMapping("/numberCode/{size}")
    public ResponseResult numberCode(@PathVariable("size")int size){
        double mathRandom = (Math.random()*9+1)*(Math.pow(10,size-1));
        int res = (int) mathRandom;
        System.out.println("生成的:  "+res);
        //定义返回值
        NumberCodeResponse response = new NumberCodeResponse();
        response.setNumberCode(res);
        return ResponseResult.success(response);
    }
}
