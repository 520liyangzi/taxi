package apiverificationcode.controller;


import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.response.NumberCodeResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class numberCodeController {

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
