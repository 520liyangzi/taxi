package com.lyz.ssedriverclient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SseController {

    public static Map<String,SseEmitter> stringSseEmitterMap = new HashMap<>();

    @GetMapping("/connect/{driverId}")
    public SseEmitter connect(@PathVariable("driverId")String driverId){
        System.out.println("司机ID： " + driverId);
        SseEmitter sseEmitter = new SseEmitter(0L);
        stringSseEmitterMap.put(driverId,sseEmitter);
        return sseEmitter;
    }

    @GetMapping("/push")
    public String push(@RequestParam String driverId,@RequestParam String content){
        try {
            if (stringSseEmitterMap.containsKey(driverId)){
                stringSseEmitterMap.get(driverId).send(content);
            }else {
                return "推送失败！";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "给用户： " + driverId +"  发送了消息： " + content;
    }

    @GetMapping("/close/{driverId}")
    public String close(@PathVariable String driverId){
    System.out.println("close方法调用了" + driverId);
        if (stringSseEmitterMap.containsKey(driverId)){
            stringSseEmitterMap.remove(driverId);
        }
        return "OK";
    }

}
