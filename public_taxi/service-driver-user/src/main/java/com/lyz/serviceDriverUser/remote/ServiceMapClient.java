package com.lyz.serviceDriverUser.remote;


import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.dto.TraceResponse;
import com.lyz.internalcommon.request.VerificationCodeDTO;
import com.lyz.internalcommon.response.TerminalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-map")
public interface ServiceMapClient {
    @RequestMapping(method = RequestMethod.POST,value = "/terminal/add")
    ResponseResult<TerminalResponse> addTerminal(@RequestParam String name, @RequestParam String desc);


    @RequestMapping(method = RequestMethod.POST,value = "/trace/add")
    ResponseResult<TraceResponse> addTrace(@RequestParam String tid);
}
