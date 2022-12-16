package com.lyz.apiDriver.service;

import com.lyz.apiDriver.remote.ServiceDriverUserClient;
import com.lyz.internalcommon.dto.DriverCarBindingRelationship;
import com.lyz.internalcommon.dto.DriverUser;
import com.lyz.internalcommon.dto.DriverUserWorkStatus;
import com.lyz.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    @Autowired
    ServiceDriverUserClient serviceDriverUserClient;

    public ResponseResult updateUser(DriverUser driverUser){
        return serviceDriverUserClient.updateUser(driverUser);
    }

    public ResponseResult changeWorkStatus(DriverUserWorkStatus driverUserWorkStatus) {
        return serviceDriverUserClient.changeWorkStatus(driverUserWorkStatus);
    }

    public ResponseResult<DriverCarBindingRelationship> getCarBindingByDriverId(String driverPhone) {
        return serviceDriverUserClient.getCarBindingByDriverId(driverPhone);
    }
}
