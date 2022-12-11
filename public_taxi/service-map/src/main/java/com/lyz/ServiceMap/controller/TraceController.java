package com.lyz.ServiceMap.controller;

import com.lyz.ServiceMap.service.TraceService;
import com.lyz.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trace")
public class TraceController {

    @Autowired
    TraceService traceService;

    @PostMapping("/add")
    public ResponseResult addTrace(String tid){
        return traceService.addTrace(tid);
    }
}
