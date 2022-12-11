package com.lyz.ServiceMap.service;

import com.lyz.ServiceMap.remote.TraceClient;
import com.lyz.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraceService {

    @Autowired
    TraceClient traceClient;
    public ResponseResult addTrace(String tid){
        return traceClient.add(tid);
    }
}
