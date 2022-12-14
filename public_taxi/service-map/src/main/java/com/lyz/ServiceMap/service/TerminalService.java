package com.lyz.ServiceMap.service;

import com.lyz.ServiceMap.remote.TerminalClient;
import com.lyz.internalcommon.dto.ResponseResult;

import com.lyz.internalcommon.response.TerminalResponse;
import com.lyz.internalcommon.response.TrearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminalService {
    @Autowired
    TerminalClient terminalClient;
    public ResponseResult<TerminalResponse> add(String name,String desc){
        return terminalClient.add(name,desc);
    }

    public ResponseResult<List<TerminalResponse>> aroundSearch(String center, Integer radius){

        return terminalClient.aroundsearch(center,radius);
    }

    public ResponseResult<TrearchResponse> trsearch(String tid, Long startTime, Long endTime) {
        return terminalClient.trsearch(tid, startTime,endTime);
    }
}
