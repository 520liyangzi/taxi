package com.lyz.ServiceMap.service;

import com.lyz.ServiceMap.remote.PointClient;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.request.PointRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    @Autowired
    PointClient pointClient;

    public ResponseResult upload(PointRequest pointRequest){

        return pointClient.upload(pointRequest);
    }
}
