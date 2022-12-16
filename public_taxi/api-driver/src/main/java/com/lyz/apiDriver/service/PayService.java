package com.lyz.apiDriver.service;

import com.lyz.apiDriver.remote.ServiceOrderClient;
import com.lyz.apiDriver.remote.ServiceSseClient;
import com.lyz.internalcommon.constant.IdentityConstant;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.request.OrderRequest;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class PayService {

    @Autowired
    ServiceSseClient serviceSseClient;

    @Autowired
    ServiceOrderClient serviceOrderClient;

    public ResponseResult pushPayInfo(String orderId,String price,Long passengerId){
        //封装消息
        JSONObject message = new JSONObject();
        message.put("price",price);
        message.put("orderId",orderId);
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderId(Long.valueOf(orderId));
        serviceOrderClient.pushPayInfo(orderRequest);

        // 推送消息
        serviceSseClient.push(passengerId, IdentityConstant.PASSENGER_IDENTITY,message.toString());

        // 返回
        return ResponseResult.success();
    }
}
