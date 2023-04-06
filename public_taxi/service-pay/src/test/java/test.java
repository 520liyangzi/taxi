import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;

@SpringBootTest
class test {

    @Resource
    RabbitTemplate rabbitTemplate;

    @Test
    public void te(){
        String ii = "1584370883330600969";
        rabbitTemplate.convertAndSend("pay.queue",ii);
    }
}
