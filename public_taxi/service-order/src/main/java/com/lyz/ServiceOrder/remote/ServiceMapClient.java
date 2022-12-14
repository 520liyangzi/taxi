package com.lyz.ServiceOrder.remote;

import com.lyz.internalcommon.dto.ResponseResult;;
import com.lyz.internalcommon.response.TerminalResponse;
import com.lyz.internalcommon.response.TrearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("service-map")
public interface ServiceMapClient {

    @RequestMapping(method = RequestMethod.POST,value = "/terminal/aroundsearch")
    public ResponseResult<List<TerminalResponse>> terminalAroundSearch(@RequestParam  String center , @RequestParam Integer radius);

    @RequestMapping(method = RequestMethod.POST,value = "/terminal/trsearch")
    ResponseResult<TrearchResponse> trsearch(@RequestParam String tid, @RequestParam Long starttime,@RequestParam Long endtime);
}
