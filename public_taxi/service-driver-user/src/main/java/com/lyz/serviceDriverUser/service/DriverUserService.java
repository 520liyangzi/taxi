package com.lyz.serviceDriverUser.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyz.internalcommon.constant.CommonStatusEnum;
import com.lyz.internalcommon.constant.DriverCarConstants;
import com.lyz.internalcommon.dto.*;
import com.lyz.internalcommon.response.OrderDriverResponse;
import com.lyz.serviceDriverUser.mapper.CarMapper;
import com.lyz.serviceDriverUser.mapper.DriverCarBindingRelationshipMapper;
import com.lyz.serviceDriverUser.mapper.DriverUserMapper;
import com.lyz.serviceDriverUser.mapper.DriverUserWorkStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DriverUserService {

    @Autowired
    DriverUserMapper driverUserMapper;

    @Autowired
    DriverUserWorkStatusMapper driverUserWorkStatusMapper;

    @Autowired
    CarMapper carMapper;

    public ResponseResult testGerDriverUser(){
        DriverUser driverUser = driverUserMapper.selectById(1);
        return ResponseResult.success(driverUser);
    }


    public ResponseResult addDriverUser(DriverUser driverUser){
        LocalDateTime now = LocalDateTime.now();
        driverUser.setGmtCreate(now);
        driverUser.setGmtModified(now);

        int insert = driverUserMapper.insert(driverUser);
        //初始化  司机工作状态表
        DriverUserWorkStatus driverUserWorkStatus = new DriverUserWorkStatus();
        driverUserWorkStatus.setDriverId(driverUser.getId());
        driverUserWorkStatus.setWorkStatus(DriverCarConstants.DRIVER_WORK_STATUS_START);
        driverUserWorkStatus.setGmtModified(now);
        driverUserWorkStatus.setGmtCreate(now);
        driverUserWorkStatusMapper.insert(driverUserWorkStatus);


        return ResponseResult.success(insert);
    }

    public ResponseResult updateDriverUser(DriverUser driverUser){
        LocalDateTime now = LocalDateTime.now();
        driverUser.setGmtModified(now);
        driverUserMapper.updateById(driverUser);
        return ResponseResult.success();
    }

    public ResponseResult<DriverUser> getDriverUser(String driverPhone){
        Map<String,Object> map = new HashMap<>();
        map.put("driver_phone",driverPhone);
        map.put("state", DriverCarConstants.DRIVER_STATE_VALID);
        List<DriverUser> driverUsers = driverUserMapper.selectByMap(map);
        if (driverUsers.isEmpty()){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_NOT_EXISTS.getCode(),CommonStatusEnum.DRIVER_NOT_EXISTS.getValue());
        }
        DriverUser driverUser = driverUsers.get(0);
        return ResponseResult.success(driverUser);
    }

    @Autowired
    DriverCarBindingRelationshipMapper driverCarBindingRelationshipMapper;

    public ResponseResult<OrderDriverResponse> getAvailiableDriver(Long carId){
        //车辆和司机绑定关系的query
        QueryWrapper<DriverCarBindingRelationship> DriverCarBindingRelationshipQueryWrapper = new QueryWrapper<>();
        DriverCarBindingRelationshipQueryWrapper.eq("car_id",carId);
        DriverCarBindingRelationshipQueryWrapper.eq("bind_state",DriverCarConstants.DRIVER_CAR_BIND);
        DriverCarBindingRelationship relationship = driverCarBindingRelationshipMapper.selectOne(DriverCarBindingRelationshipQueryWrapper);
        Long driverId = relationship.getDriverId();

        QueryWrapper<DriverUserWorkStatus> DriverUserWorkStatusQueryWrapper = new QueryWrapper<>();
        DriverUserWorkStatusQueryWrapper.eq("driver_id",driverId);
        DriverUserWorkStatusQueryWrapper.eq("work_status",DriverCarConstants.DRIVER_WORK_STATUS_START);
        DriverUserWorkStatus driverUserWorkStatus = driverUserWorkStatusMapper.selectOne(DriverUserWorkStatusQueryWrapper);
        if(driverUserWorkStatus == null){
            return ResponseResult.fail(CommonStatusEnum.AVAILIABLE_DRIVER_EMPTY.getCode(),CommonStatusEnum.AVAILIABLE_DRIVER_EMPTY.getValue());
        }

        QueryWrapper<DriverUser> DriverUserQueryWrapper = new QueryWrapper<>();
        DriverUserQueryWrapper.eq("id",driverId);
        DriverUser driverUser = driverUserMapper.selectOne(DriverUserQueryWrapper);

        QueryWrapper<Car> carQueryWrapper = new QueryWrapper<>();
        carQueryWrapper.eq("id",carId);
        Car car = carMapper.selectOne(carQueryWrapper);


        OrderDriverResponse orderDriverResponse = new OrderDriverResponse();
        orderDriverResponse.setCarId(carId);
        orderDriverResponse.setDriverId(driverId);
        orderDriverResponse.setDriverPhone(driverUser.getDriverPhone());
        orderDriverResponse.setVehicleType(car.getVehicleType());
        orderDriverResponse.setLicenseId(driverUser.getLicenseId());
        orderDriverResponse.setVehicleNo(car.getVehicleNo());

        return ResponseResult.success(orderDriverResponse);
    }
}
