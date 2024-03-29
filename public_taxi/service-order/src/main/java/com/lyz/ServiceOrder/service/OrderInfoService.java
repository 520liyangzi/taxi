package com.lyz.ServiceOrder.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyz.ServiceOrder.remote.ServiceDriverUserClient;
import com.lyz.ServiceOrder.remote.ServiceMapClient;
import com.lyz.ServiceOrder.remote.ServicePriceClient;
import com.lyz.ServiceOrder.remote.ServiceSseClient;
import com.lyz.internalcommon.constant.CommonStatusEnum;
import com.lyz.internalcommon.constant.DriverCarConstants;
import com.lyz.internalcommon.constant.IdentityConstant;
import com.lyz.internalcommon.constant.OrderConstant;
import com.lyz.internalcommon.dto.Car;
import com.lyz.internalcommon.dto.OrderInfo;
import com.lyz.ServiceOrder.mapper.OrderInfoMapper;
import com.lyz.internalcommon.dto.PriceRule;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.request.OrderRequest;
import com.lyz.internalcommon.response.OrderDriverResponse;
import com.lyz.internalcommon.response.TerminalResponse;
import com.lyz.internalcommon.response.TrearchResponse;
import com.lyz.internalcommon.util.RedisPrefixUtils;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
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

    @Autowired
    ServiceSseClient serviceSseClient;


    /**
     * 新建订单
     * @param orderRequest
     * @return
     */

    @GlobalTransactional
    public ResponseResult add(OrderRequest orderRequest){

        System.out.println("???");
        //下单需要判断设备是否是黑名单设备
        String deviceCode = orderRequest.getDeviceCode();

        //生成key
        String deviceCodeKey = RedisPrefixUtils.blackDeviceCodePrefix + deviceCode;

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

        if(!isAvailableDriver(orderRequest)){
            return ResponseResult.fail(CommonStatusEnum.NO_DRIVER.getCode(),CommonStatusEnum.NO_DRIVER.getValue());
            //这里根据城市编码 去生成的视图里查  视图里有driver id， city code，work status
        }
        System.out.println("有司机");

        // 是否在进行
        if(isOrderGoingOn(orderRequest.getPassengerId()) > 0){
      System.out.println(orderRequest.getPassengerId() + "---");
            return ResponseResult.fail(CommonStatusEnum.ORDER_NOT_CREATE.getCode(),CommonStatusEnum.ORDER_NOT_CREATE.getValue());
        }
        System.out.println("是否在工作");

        OrderInfo orderInfo = new OrderInfo();

        BeanUtils.copyProperties(orderRequest,orderInfo);

        orderInfo.setOrderStatus(OrderConstant.ORDER_STSRT);
        LocalDateTime now = LocalDateTime.now();
        orderInfo.setGmtCreate(now);
        orderInfo.setGmtModified(now);

        orderInfoMapper.insert(orderInfo);

        log.error("现在已经是插入成功了！！！！！！！！！！！！！！！！");

        //定时任务处理
        for(int i = 0;i<6;i++){
            //派单
            int res = dispatchRealTimeOrder(orderInfo);
            if(res == 1){
                break;
            }
            if (i == 5){
        // 没办法了  订单无效
        System.out.println("还没有找到哇！！！");
                orderInfo.setOrderStatus(OrderConstant.ORDER_INVALIT);
                orderInfoMapper.updateById(orderInfo);
            }else {
                //等等20秒 再找一波
                System.out.println("派单");
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return ResponseResult.success();
    }

    @Autowired
    ServiceMapClient serviceMapClient;

    @Autowired
    RedissonClient redissonClient;
    /**
     * 实时订单派单逻辑
     * @param orderInfo
     */
    public synchronized int dispatchRealTimeOrder(OrderInfo orderInfo){
        log.info("循环一次");
        int res = 0;
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
            JSONArray result = JSONArray.fromObject(data);
            for(int j = 0; j < result.size();j++){
                JSONObject jsonObject = result.getJSONObject(j);
                String carIdString = jsonObject.getString("carId");
                Long carId = Long.parseLong(carIdString);
                String longitude = jsonObject.getString("longitude");
                String latitude = jsonObject.getString("latitude");

        // 找到车了  再看看司机的信息 能不能用呢？
        System.out.println("让我来看看carId是多少" + carId);
                //根据ID  查询对应是否有对应的可派单司机
                ResponseResult<OrderDriverResponse> availiableDriver = serviceDriverUserClient.getAvailiableDriver(carId);
                if(availiableDriver == null){
          System.out.println("没有对应可派单司机");
          continue UI;
                }
                if (availiableDriver.getCode() == 1607){
                    log.info("没有对应的司机    " + carId);
                    continue UI;
                }else if(availiableDriver.getCode() == 16087){
                    log.info("都他妈没有绑定关系");
                    continue UI;
                }else{
                    //出车  +  没有订单
                    log.info("找到了正在出车的司机  车辆ID是 " + carId);
                    OrderDriverResponse orderDriverResponse = availiableDriver.getData();
                    Long driverId = orderDriverResponse.getDriverId();
                    //判断司机是否有正在进行中的订单

                    String vehicleTypeFromCar = orderDriverResponse.getVehicleType();
          System.out.println("让我来看看我公司车型" + vehicleTypeFromCar);
                    //看看车型是否符合
                    String vehicleType = orderInfo.getVehicleType();
                    System.out.println("让我来看看你下单的车型" + vehicleType);
                    if (!vehicleTypeFromCar.trim().equals(vehicleType.trim())){
            System.out.println("车型不符合！！！！！！！！！！！！！！！！");
                        continue;
                    }


                    //不管原来指向哪里  最终都是从常量池中拿出一个引用地址  只有一个地方
                    //如果没有intern  则可能有很多入口 虽然有锁
                    String lockKey = (driverId + "").intern();
                    RLock lock = redissonClient.getLock(lockKey);
                    lock.lock();
//                    synchronized ((driverId + "").intern()){
                        if(isDriverOrderGoingOn(driverId) > 0){
                            lock.unlock();
                            log.info("司机他妈的有个订单了  操！！！");
                            continue;
                        }

                    //订单直接匹配司机   把订单中与司机相关的信息补全
                        QueryWrapper<Car> queryWrapper = new QueryWrapper<Car>();
                        queryWrapper.eq("id",carId);
                        LocalDateTime now = LocalDateTime.now();
                        orderInfo.setDriverId(driverId);
                        orderInfo.setDriverPhone(orderDriverResponse.getDriverPhone());
                        orderInfo.setCarId(orderDriverResponse.getCarId());
                        orderInfo.setReceiveOrderCarLongitude(longitude);
                        orderInfo.setReceiveOrderCarLatitude(latitude);
                        orderInfo.setReceiveOrderTime(now);
                        orderInfo.setLicenseId(orderDriverResponse.getLicenseId());
                        orderInfo.setVehicleNo(orderDriverResponse.getVehicleNo());
                        orderInfo.setOrderStatus(OrderConstant.DRIVER_RECEIVE_ORDER); //接单
                        orderInfoMapper.updateById(orderInfo);

                    // 通知司机
                    JSONObject driverContent = new JSONObject();
                    driverContent.put("orderId",orderInfo.getId());
                    driverContent.put("passengerId",orderInfo.getPassengerId());
                    driverContent.put("passengerPhone",orderInfo.getPassengerPhone());
                    driverContent.put("departure",orderInfo.getDeparture());
                    driverContent.put("depLongitude",orderInfo.getDepLongitude());
                    driverContent.put("depLatitude",orderInfo.getDepLatitude());

                    driverContent.put("destination",orderInfo.getDestination());
                    driverContent.put("destLongitude",orderInfo.getDestLongitude());
                    driverContent.put("destLatitude",orderInfo.getDestLatitude());

                    serviceSseClient.push(driverId,IdentityConstant.DRIVER_IDENTITY,driverContent.toString());

                    // 通知乘客
                    JSONObject passengerContent = new  JSONObject();
                    passengerContent.put("orderId",orderInfo.getId());
                    passengerContent.put("driverId",orderInfo.getDriverId());
                    passengerContent.put("driverPhone",orderInfo.getDriverPhone());
                    passengerContent.put("vehicleNo",orderInfo.getVehicleNo());
                    // 车辆信息，调用车辆服务
                    ResponseResult<Car> carById = serviceDriverUserClient.getCarById(carId);
                    Car carRemote = carById.getData();

                    passengerContent.put("brand", carRemote.getBrand());
                    passengerContent.put("model",carRemote.getModel());
                    passengerContent.put("vehicleColor",carRemote.getVehicleColor());

                    passengerContent.put("receiveOrderCarLongitude",orderInfo.getReceiveOrderCarLongitude());
                    passengerContent.put("receiveOrderCarLatitude",orderInfo.getReceiveOrderCarLatitude());
                    serviceSseClient.push(orderInfo.getPassengerId(),IdentityConstant.PASSENGER_IDENTITY,passengerContent.toString());
                    res = 1;
                        lock.unlock();

                        break UI;
//                    }
                }
            }
        }
        return res;
    }



    /**
     * 收否有可用司机
     * @param orderRequest
     * @return
     */
    private boolean isAvailableDriver(OrderRequest orderRequest){
        System.out.println("---");
        ResponseResult<Boolean> availableDriver = serviceDriverUserClient.isAvailableDriver(orderRequest.getAddress());
        log.info(availableDriver.getData() + "有没有！！！！！！！！！！");
        return availableDriver.getData();
    }

    /**
     * 计价规则收否存在
     * @param orderRequest
     * @return
     */
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

    /**
     * 收否是黑名单
     * @param deviceCodeKey
     * @return
     */
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

    /**
     * 订单是否在进行
     * @param passengerId
     * @return
     */
    private Long isOrderGoingOn(Long passengerId){
    System.out.println("进入了 isordergoingon");
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


    /**
     * 司机单是否在进行
     * @param driverId
     * @return
     */
    private Long isDriverOrderGoingOn(Long driverId){
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("driver_id",driverId);
        queryWrapper.and(wrapper -> wrapper.eq("order_status", OrderConstant.DRIVER_RECEIVE_ORDER)
                .or().eq("order_status", OrderConstant.DRIVER_TO_PICKUP_PASSENGER)
                .or().eq("order_status", OrderConstant.DRIVER_ARRIVED_DEPARTURE)
                .or().eq("order_status", OrderConstant.PICK_UP_PASSENGER));

        Long aLong = orderInfoMapper.selectCount(queryWrapper);
        log.info("司机Id ：" + driverId +"正在进行的订单数量是 " + aLong);
        return aLong;
    }


    /**
     * 去接乘客
     * @param orderRequest
     * @return
     */
    public ResponseResult toPickUpPassenger(@RequestBody OrderRequest orderRequest){
        Long orderId = orderRequest.getOrderId();
        String toPickUpPassengerLongitude = orderRequest.getToPickUpPassengerLongitude();
        String toPickUpPassengerLatitude = orderRequest.getToPickUpPassengerLatitude();
        String toPickUpPassengerAddress = orderRequest.getToPickUpPassengerAddress();
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id",orderId);
        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);
        orderInfo.setToPickUpPassengerAddress(toPickUpPassengerAddress);
        orderInfo.setToPickUpPassengerLatitude(toPickUpPassengerLatitude);
        orderInfo.setToPickUpPassengerLongitude(toPickUpPassengerLongitude);
        orderInfo.setToPickUpPassengerTime(LocalDateTime.now());
        orderInfo.setOrderStatus(OrderConstant.DRIVER_TO_PICKUP_PASSENGER);

        orderInfoMapper.updateById(orderInfo);
        return ResponseResult.success();
    }


    /**
     * 到达乘客目的地
     * @param orderRequest
     * @return
     */
    @PostMapping("/arrived-departure")
    public ResponseResult arrivedDeparture(OrderRequest orderRequest){
        Long orderId = orderRequest.getOrderId();
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderId);
        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);
        orderInfo.setDriverArrivedDepartureTime(LocalDateTime.now());
        orderInfo.setOrderStatus(OrderConstant.DRIVER_ARRIVED_DEPARTURE);
        orderInfoMapper.updateById(orderInfo);
        return ResponseResult.success();
    }

    /**
     * 司机接到乘客
     * @param orderRequest
     * @return
     */
    public ResponseResult pickUpPassenger(OrderRequest orderRequest) {
    System.out.println(orderRequest);
        Long orderId = orderRequest.getOrderId();
        String pickUpPassengerLongitude = orderRequest.getPickUpPassengerLongitude();
        String pickUpPassengerLatitude = orderRequest.getPickUpPassengerLatitude();
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderId);
        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);
        orderInfo.setPickUpPassengerTime(LocalDateTime.now());
        orderInfo.setPickUpPassengerLongitude(pickUpPassengerLongitude);
        orderInfo.setPickUpPassengerLatitude(pickUpPassengerLatitude);
        orderInfo.setOrderStatus(OrderConstant.PICK_UP_PASSENGER);
        orderInfoMapper.updateById(orderInfo);
        return ResponseResult.success();
    }


    /**
     * 乘客下床到达目的地   形成终止
     * @param orderRequest
     * @return
     */
    public ResponseResult passengerGetoff(OrderRequest orderRequest) {
        System.out.println(orderRequest);
        Long orderId = orderRequest.getOrderId();
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderId);
        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);

        orderInfo.setPassengerGetoffTime(LocalDateTime.now());
        orderInfo.setPassengerGetoffLongitude(orderRequest.getPassengerGetoffLongitude());
        orderInfo.setPassengerGetoffLatitude(orderRequest.getPassengerGetoffLatitude());
        orderInfo.setOrderStatus(OrderConstant.PASSENGER_GETOFF);

        //订单形式的路程和时间  用service-map来写
        ResponseResult<Car> carById = serviceDriverUserClient.getCarById(orderInfo.getCarId());
    System.out.println(orderInfo.getPickUpPassengerTime());
    System.out.println(orderInfo.getPassengerGetoffTime());
        long l = orderInfo.getPickUpPassengerTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long l1 = orderInfo.getPassengerGetoffTime().toInstant(ZoneOffset.of("+0")).toEpochMilli();
        System.out.println(l + "---");
        System.out.println(l1 + "---");
        ResponseResult<TrearchResponse> trsearch = serviceMapClient.trsearch(
                carById.getData().getTid(), l, l1);
    System.out.println(trsearch+"=======");
        TrearchResponse data = trsearch.getData();
        long driveTime = data.getDriveTime();
        long driveMile = data.getDriveMile();
        orderInfo.setDriveTime(driveTime);
        orderInfo.setDriveMile(driveMile);
        double time = driveTime / 1000 / 60;
        if (time == 0){
            time += 1;
        }
        //获取最终价格
        ResponseResult<Double> doubleResponseResult = servicePriceClient.calculatePrice((int) driveMile, (int) time, orderInfo.getAddress(), orderInfo.getVehicleType());
        Double price = doubleResponseResult.getData();
    System.out.println(price+"oooooooooooooo");
        orderInfo.setPrice(price);
        orderInfoMapper.updateById(orderInfo);
        return ResponseResult.success();
    }


    /**
     * 支付完成
     * @param orderRequest
     * @return
     */
    public ResponseResult pay(OrderRequest orderRequest) {
        Long orderId = orderRequest.getOrderId();
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        orderInfo.setOrderStatus(OrderConstant.SUCCESS_PAY);
        orderInfoMapper.updateById(orderInfo);
        return ResponseResult.success();
    }

    public ResponseResult cancel(Long orderId, String identity) {
        //查询订单状态
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        Integer orderStatus = orderInfo.getOrderStatus();
        LocalDateTime cancelTime = LocalDateTime.now();
        Integer cancelOperator = null;
        Integer cancelTypeCode = null;

        // 正常取消
        int cancelType = 1;

        //更新订单的取消状态
        //如果乘客取消
        if(identity.trim().equals(IdentityConstant.PASSENGER_IDENTITY)){
            switch (orderStatus){
                case OrderConstant.ORDER_STSRT:
                    cancelTypeCode = OrderConstant.CANCEL_PASSENGER_BEFORE;
                    break;

                // 司机接到订单
                case OrderConstant.DRIVER_RECEIVE_ORDER:
                    LocalDateTime receiveOrderTime = orderInfo.getReceiveOrderTime();
                    long between = ChronoUnit.MINUTES.between(receiveOrderTime, cancelTime);
                    if (between > 1){
                        cancelTypeCode = OrderConstant.CANCEL_PASSENGER_ILLEGAL;
                    }else {
                        cancelTypeCode = OrderConstant.CANCEL_PASSENGER_BEFORE;
                    }
                    break;
                case OrderConstant.DRIVER_TO_PICKUP_PASSENGER:
                    // 司机到达乘客起点
                case OrderConstant.DRIVER_ARRIVED_DEPARTURE:
                    cancelTypeCode = OrderConstant.CANCEL_PASSENGER_ILLEGAL;
                    break;

                default:
                    log.info("乘客取消失败");
                    cancelType = 0;
                    break;
            }
        }

        // 如果是司机取消
        if (identity.trim().equals(IdentityConstant.DRIVER_IDENTITY)){
            switch (orderStatus){
                // 订单开始
                // 司机接到乘客
                case OrderConstant.DRIVER_RECEIVE_ORDER:
                case OrderConstant.DRIVER_TO_PICKUP_PASSENGER:
                case OrderConstant.DRIVER_ARRIVED_DEPARTURE:
                    LocalDateTime receiveOrderTime = orderInfo.getReceiveOrderTime();
                    long between = ChronoUnit.MINUTES.between(receiveOrderTime, cancelTime);
                    if (between > 1){
                        cancelTypeCode = OrderConstant.CANCEL_DRIVER_ILLEGAL;
                    }else {
                        cancelTypeCode = OrderConstant.CANCEL_DRIVER_BEFORE;
                    }
                    break;

                default:
                    log.info("司机取消失败");
                    cancelType = 0;
                    break;
            }
        }

        if (cancelType == 0){
            return ResponseResult.fail(CommonStatusEnum.ORDER_CANCEL_ERROR.getCode(),CommonStatusEnum.ORDER_CANCEL_ERROR.getValue());
        }

        orderInfo.setCancelTypeCode(cancelTypeCode);
        orderInfo.setCancelTime(cancelTime);
        orderInfo.setCancelOperator(Integer.parseInt(identity));
        orderInfo.setOrderStatus(OrderConstant.ORDER_CANCLE);

        orderInfoMapper.updateById(orderInfo);
        return ResponseResult.success();
    }

    public ResponseResult pushPayInfo(OrderRequest orderRequest) {
        Long orderId = orderRequest.getOrderId();
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        orderInfo.setOrderStatus(OrderConstant.TO_START_PAY);
        orderInfoMapper.updateById(orderInfo);
        return ResponseResult.success();
    }
}
