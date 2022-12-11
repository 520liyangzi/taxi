package com.lyz.apiDriver.service;

import com.lyz.apiDriver.remote.ServiceDriverUserClient;
import com.lyz.apiDriver.remote.ServiceMapClient;
import com.lyz.internalcommon.dto.Car;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.request.ApiDriverPointRequest;
import com.lyz.internalcommon.request.PointRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointService {
    @Autowired
    ServiceDriverUserClient serviceDriverUserClient;

    @Autowired
    ServiceMapClient serviceMapClient;

    public ResponseResult upload(ApiDriverPointRequest apiDriverPointRequest){

        //获取carId
        Long carId = apiDriverPointRequest.getCarId();
        //通过carId获取tid和trid  调用service-driver-user接口
        System.out.println(carId);
        ResponseResult<Car> carbyId = serviceDriverUserClient.getCarbyId(carId);
        Car car = carbyId.getData();
        String tid = car.getTid();
        String trid = car.getTrid();
        System.out.println(tid+"   "+trid);

        //调用地图去上传 service-map接口
        PointRequest request = new PointRequest();
        request.setTid(tid);
        request.setTrid(trid);
        request.setPoints(apiDriverPointRequest.getPoints());

        return serviceMapClient.upload(request);
    }
}
