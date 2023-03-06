package testalipay.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class a {
    // https://6i587977f2.zicp.fun/alipay/notify
    @GetMapping("/test")
    public String t(){
    System.out.println("回调了");
    return "穿透";
    }
}
