package com.lyz.ServiceMap.service;

import com.lyz.ServiceMap.remote.ServiceClient;
import com.lyz.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceMapService {

    @Autowired
    ServiceClient serviceClient;
    public ResponseResult add(String name){

        return serviceClient.add(name);
    }
}
