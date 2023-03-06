package com.lyz.ssedriverclient.controller;

import com.lyz.internalcommon.util.SsePrefixUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class SseController {

    public static Map<String,SseEmitter> stringSseEmitterMap = new HashMap<>();
    // 用静态的map来存哦

    @GetMapping("/connect")
    public SseEmitter connect(@RequestParam Long userId,@RequestParam String identity){
        // 进行前后端交互
        log.info("用户Id是 " + userId + "身份类型是 "+ identity);
        SseEmitter sseEmitter = new SseEmitter(0L);
        // 永不超时  除非自己关闭

        String mapKey = SsePrefixUtils.gengeratorKey(userId,identity);
        stringSseEmitterMap.put(mapKey,sseEmitter);
        return sseEmitter;
    }

    @GetMapping("/push")
    public String push(@RequestParam Long userId,@RequestParam String identity,@RequestParam String content){
        log.info(userId + " ======" + identity);
        log.info(content);
        String mapKey = SsePrefixUtils.gengeratorKey(userId,identity);
        try {
            if (stringSseEmitterMap.containsKey(mapKey)){
                stringSseEmitterMap.get(mapKey).send(content);
                // 把消息发送出去！！！
            }else {
                return "推送失败！";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "给用户： " + mapKey +"  发送了消息： " + content;
    }

    @GetMapping("/close")
    public String close(@RequestParam Long userId,@RequestParam String identity){
        String mapKey = SsePrefixUtils.gengeratorKey(userId,identity);
    System.out.println("close方法调用了" + mapKey);
        if (stringSseEmitterMap.containsKey(mapKey)){
            stringSseEmitterMap.remove(mapKey);
        }
        return "OK";
    }

}
