package com.lyz.apiBoss.service;

import com.lyz.apiBoss.remote.ServiceDriverUserClient;
import com.lyz.internalcommon.dto.DriverUser;
import com.lyz.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverUserService {

    @Autowired
    ServiceDriverUserClient serviceDriverUserClient;


    public ResponseResult addDriverUser(DriverUser driverUser){
        return serviceDriverUserClient.addDriverUser(driverUser);
    }

    public ResponseResult updateDriverUser(DriverUser driverUser){
        return serviceDriverUserClient.updateDriverUser(driverUser);
    }
}
