package com.lyz.ServiceOrder.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyz.ServiceOrder.remote.ServiceDriverUserClient;
import com.lyz.ServiceOrder.remote.ServiceMapClient;
import com.lyz.ServiceOrder.remote.ServicePriceClient;
import com.lyz.internalcommon.constant.CommonStatusEnum;
import com.lyz.internalcommon.constant.OrderConstant;
import com.lyz.internalcommon.dto.OrderInfo;
import com.lyz.ServiceOrder.mapper.OrderInfoMapper;
import com.lyz.internalcommon.dto.PriceRule;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.request.OrderRequest;
import com.lyz.internalcommon.response.OrderDriverResponse;
import com.lyz.internalcommon.response.TerminalResponse;
import com.lyz.internalcommon.util.RedisPrefixUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Slf4j
@Service
public class OrderInfoService {
    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ServicePriceClient servicePriceClient;

    @Autowired
    ServiceDriverUserClient serviceDriverUserClient;

    public ResponseResult add(OrderRequest orderRequest){

        System.out.println("???");
        //下单需要判断设备是否是黑名单设备
        String deviceCode = orderRequest.getDeviceCode();

        //生成key
        String deviceCodeKey = RedisPrefixUtils.blackDeviceCodePrefix + deviceCode;

        if(!isAvailableDriver(orderRequest)){
            return ResponseResult.fail(CommonStatusEnum.NO_DRIVER.getCode(),CommonStatusEnum.NO_DRIVER.getValue());
        }
        System.out.println("有司机");

        //设置key  看原来有没有
        if(isBlackDevice(deviceCodeKey)){
            return ResponseResult.fail(CommonStatusEnum.BLACK.getCode(),CommonStatusEnum.BLACK.getValue());
        }
        System.out.println("黑名单");

        //下单城市和计价规则是否正常
        if(!isPriceRuleExists(orderRequest)){
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_EMPTY.getCode(),CommonStatusEnum.PRICE_RULE_EMPTY.getValue());
        }
        System.out.println("正常");


        // 是否在进行
        if(isOrderGoingOn(orderRequest.getPassengerId()) > 0){
            return ResponseResult.fail(CommonStatusEnum.ORDER_NOT_CREATE.getCode(),CommonStatusEnum.ORDER_NOT_CREATE.getValue());
        }
        System.out.println("是否在进行");

        OrderInfo orderInfo = new OrderInfo();

        BeanUtils.copyProperties(orderRequest,orderInfo);

        orderInfo.setOrderStatus(OrderConstant.ORDER_STSRT);
        LocalDateTime now = LocalDateTime.now();
        orderInfo.setGmtCreate(now);
        orderInfo.setGmtModified(now);

        orderInfoMapper.insert(orderInfo);


        //派单
        dispatchRealTimeOrder(orderInfo);
        System.out.println("派单");

        return ResponseResult.success();
    }

    @Autowired
    ServiceMapClient serviceMapClient;
    /**
     * 实时订单派单逻辑
     * @param orderInfo
     */
    public void dispatchRealTimeOrder(OrderInfo orderInfo){
        String depLatitude = orderInfo.getDepLatitude();
        String depLongitude = orderInfo.getDepLongitude();
        String center = depLatitude + "," + depLongitude;
        List<Integer> raduisList = new ArrayList<>();
        raduisList.add(2000);
        raduisList.add(4000);
        raduisList.add(5000);
        ResponseResult<List<TerminalResponse>> listResponseResult = null;
        UI : for (int i = 0 ; i < raduisList.size();i++){
            listResponseResult = serviceMapClient.terminalAroundSearch(center, raduisList.get(i));
            List<TerminalResponse> data = listResponseResult.getData();
            log.info("寻找车辆   半径: "+ raduisList.get(i));
            System.out.println(data);
            JSONArray result = JSONArray.fromObject(data);
            for(int j = 0; j < result.size();j++){
                JSONObject jsonObject = result.getJSONObject(j);
                String carIdString = jsonObject.getString("carId");
                Long carId = Long.parseLong(carIdString);

                //根据ID  查询对应是否有对应的可派单司机
                ResponseResult<OrderDriverResponse> availiableDriver = serviceDriverUserClient.getAvailiableDriver(carId);
                if (availiableDriver.getCode() == 1607){
                    log.info("没有对应的司机    " + carId);
                    continue UI;
                }else {
                    log.info("找到了正在出车的司机  车辆ID是 " + carId);
                    break UI;
                }
            }
        }
    }

    ///city-driver/is-available-driver
    private boolean isAvailableDriver(OrderRequest orderRequest){
        System.out.println("---");
        ResponseResult<Boolean> availableDriver = serviceDriverUserClient.isAvailableDriver(orderRequest.getAddress());
        log.info(availableDriver.getData() + "有没有！！！！！！！！！！");
        return availableDriver.getData();
    }

    private boolean isPriceRuleExists(OrderRequest orderRequest){
        System.out.println(orderRequest);
        String fareType = orderRequest.getFareType();
        int index = fareType.indexOf("$");
        String cityCode = fareType.substring(0, index);
        String vehicleCode = fareType.substring(index+1);
        PriceRule priceRule = new PriceRule();
        priceRule.setCityCode(cityCode);
        priceRule.setVehicleType(vehicleCode);
        System.out.println("我尼玛");
        ResponseResult<Boolean> booleanResponseResult = servicePriceClient.ifPriceExists(priceRule);
        return booleanResponseResult.getData();
    }

    private boolean isBlackDevice(String deviceCodeKey){
        Boolean aBoolean = stringRedisTemplate.hasKey(deviceCodeKey);
        if(aBoolean){
            String s = stringRedisTemplate.opsForValue().get(deviceCodeKey);
            int i = Integer.parseInt(s);
            if(i > 2){
                return true;
            }else {
                stringRedisTemplate.opsForValue().increment(deviceCodeKey);
            }
        }else {
            stringRedisTemplate.opsForValue().setIfAbsent(deviceCodeKey,"1",1L, TimeUnit.HOURS);
        }
        return false;
    }

    private Long isOrderGoingOn(Long passengerId){
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("passenger_id",passengerId);
        queryWrapper.and(wrapper -> wrapper.eq("order_status", OrderConstant.ORDER_STSRT)
                .or().eq("order_status", OrderConstant.DRIVER_RECEIVE_ORDER)
                .or().eq("order_status", OrderConstant.DRIVER_TO_PICKUP_PASSENGER)
                .or().eq("order_status", OrderConstant.DRIVER_ARRIVED_DEPARTURE)
                .or().eq("order_status", OrderConstant.PICK_UP_PASSENGER)
                .or().eq("order_status", OrderConstant.PASSENGER_GETOFF)
                .or().eq("order_status", OrderConstant.TO_START_PAY));

        Long aLong = orderInfoMapper.selectCount(queryWrapper);
        return aLong;

    }

}
