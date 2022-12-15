package com.lyz.apipassenger.remote;

import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.request.OrderRequest;
import com.lyz.internalcommon.request.VerificationCodeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("service-order")
public interface ServiceOrderClient {
    @RequestMapping(method = RequestMethod.POST,value = "/order/add")
    ResponseResult add(@RequestBody OrderRequest orderRequest);

    @RequestMapping(method = RequestMethod.GET,value = "/test-real-time-order/{orderId}")
    ResponseResult t(@PathVariable("orderId")long orderId);

    @RequestMapping(method = RequestMethod.POST,value = "/order/cancel")
    ResponseResult cancel(@RequestParam Long orderId,@RequestParam String identity);
}
