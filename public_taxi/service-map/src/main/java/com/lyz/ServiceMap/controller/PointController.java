package com.lyz.ServiceMap.controller;

import com.lyz.ServiceMap.service.PointService;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.request.PointRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/point")
public class PointController {

    @Autowired
    PointService pointService;

    @PostMapping("/upload")
    public ResponseResult pointUpload(@RequestBody PointRequest pointRequest){
        return pointService.upload(pointRequest);
    }
}
