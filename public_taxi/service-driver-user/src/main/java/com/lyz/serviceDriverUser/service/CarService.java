package com.lyz.serviceDriverUser.service;

import com.lyz.internalcommon.dto.Car;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.dto.TraceResponse;
import com.lyz.internalcommon.response.TerminalResponse;
import com.lyz.serviceDriverUser.mapper.CarMapper;
import com.lyz.serviceDriverUser.remote.ServiceMapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CarService {

    @Autowired
    CarMapper carMapper;

    @Autowired
    ServiceMapClient serviceMapClient;

    public ResponseResult<TerminalResponse> addCar(Car car){
        carMapper.insert(car);
        ResponseResult<TerminalResponse> result = serviceMapClient.addTerminal(car.getVehicleNo(),car.getId()+"");
        TerminalResponse tid = result.getData();

        ResponseResult<TraceResponse> traceResponseResponseResult = serviceMapClient.addTrace(tid.getTid());

        car.setTid(tid.getTid());
        car.setTrid(traceResponseResponseResult.getData().getTrid());
        //获得轨迹trid

        carMapper.updateById(car);

        return ResponseResult.success();
    }

    public ResponseResult<Car> getCarbyId(Long carId){
        Map<String,Object> map = new HashMap<>();
        map.put("id",carId);
        List<Car> cars = carMapper.selectByMap(map);
        return ResponseResult.success(cars.get(0));
    }
}
