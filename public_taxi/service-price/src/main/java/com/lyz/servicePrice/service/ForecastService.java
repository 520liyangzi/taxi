package com.lyz.servicePrice.service;

import com.lyz.internalcommon.constant.CommonStatusEnum;
import com.lyz.internalcommon.dto.PriceRule;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.request.ForecastPriceDTO;
import com.lyz.internalcommon.response.DirectionResponse;
import com.lyz.internalcommon.response.ForecastPriceResponse;
import com.lyz.internalcommon.util.BigDecimalUtils;
import com.lyz.servicePrice.mapper.PriceRuleMapper;
import com.lyz.servicePrice.remote.ServiceMapClint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ForecastService {
    @Autowired
    private ServiceMapClint serviceMapClint;

    @Autowired
    private PriceRuleMapper priceRuleMapper;

    /**
     * 计算预估价格
     * @param depLongitude
     * @param depLatitude
     * @param destLongitude
     * @param destLatitude
     * @param cityCode
     * @param vehicleType
     * @return
     */
    public ResponseResult forecastPrice(String depLongitude, String depLatitude, String destLongitude, String destLatitude,
                                        String cityCode,String vehicleType){

        log.info("出发地经度 :  " +depLongitude);
        log.info("出发地维度 :  " + depLatitude);
        log.info("目的地经度:  " + destLongitude);
        log.info("目的地维度 :  " + destLatitude);
        ForecastPriceDTO forecastPriceDTO = new ForecastPriceDTO();
        forecastPriceDTO.setDepLongitude(depLongitude);
        forecastPriceDTO.setDepLatitude(depLatitude);
        forecastPriceDTO.setDestLongitude(destLongitude);
        forecastPriceDTO.setDestLatitude(destLatitude);

        log.info("调用地图服务，查询距离和市场");
        ResponseResult<DirectionResponse> dirction = serviceMapClint.dirction(forecastPriceDTO);
        Integer distance = dirction.getData().getDistance();
        Integer duraction = dirction.getData().getDuraction();
        log.info("distance is  "+ distance +"-------"+"duraction is   "+duraction);

        log.info("读取佳佳规则");
        Map<String,Object> map = new HashMap<>();
        map.put("city_code",cityCode);
        map.put("vehicle_type",vehicleType);
        List<PriceRule> priceRules = priceRuleMapper.selectByMap(map);
        if (priceRules.size()==0){
            //  这里就看了城市有没有开通业务
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_EMPTY.getCode(),CommonStatusEnum.PRICE_RULE_EMPTY.getValue());
        }
        PriceRule priceRule =  priceRules.get(priceRules.size()-1);

        log.info("根据距离 市场和计价规则   计算价格" + priceRule.getUnitPricePerMile());
        double price = getPrice(distance, duraction, priceRule);

        ForecastPriceResponse forecastPriceResponse = new ForecastPriceResponse();
        forecastPriceResponse.setPrice(price);
        forecastPriceResponse.setCityCode(cityCode);
        forecastPriceResponse.setVehicleType(vehicleType);
        return ResponseResult.success(forecastPriceResponse);
    }

    public ResponseResult<Double> calculatePrice(Integer distance,Integer duration,String cityCode,String vehicleType){
        log.info("读取佳佳规则");
        Map<String,Object> map = new HashMap<>();
        map.put("city_code",cityCode);
        map.put("vehicle_type",vehicleType);
    System.out.println(map);
        List<PriceRule> priceRules = priceRuleMapper.selectByMap(map);
        if (priceRules.size()==0){
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_EMPTY.getCode(),CommonStatusEnum.PRICE_RULE_EMPTY.getValue());
        }
    System.out.println(priceRules);
        PriceRule priceRule =  priceRules.get(priceRules.size()-1);
    System.out.println(priceRule);
        log.info("根据距离 市场和计价规则   计算价格");
        double price = getPrice(distance, duration, priceRule);
        return ResponseResult.success(price);
    }

    /**
     * 根据距离时长计算实际规则
     * @param distance
     * @param duration
     * @param priceRule
     * @return
     */
    public  double getPrice(Integer distance,Integer duration,PriceRule priceRule){
        double price = 0;

        //起步价
        double startFare = priceRule.getStartFare();
        price = BigDecimalUtils.add(startFare,price);

        //里程费
        double distanceMile = BigDecimalUtils.devide(distance,1000);

        double startMile = priceRule.getStartMile();

        double distanceSubtract = BigDecimalUtils.subtrack(distanceMile,startMile);
        double mile = distanceSubtract < 0 ? 0 : distanceSubtract;

        //计算
        double unitPricePerMile = priceRule.getUnitPricePerMile();
        double mileFare = BigDecimalUtils.multiply(mile,unitPricePerMile);
        //里程价

        price = BigDecimalUtils.add(price,mileFare);


        double time = BigDecimalUtils.devide(duration,60);

        double unitPricePerMinute = priceRule.getUnitPricePerMinute();
        double timeFare = BigDecimalUtils.multiply(time,unitPricePerMinute);
        price = BigDecimalUtils.add(price,timeFare);

        BigDecimal bigDecimal = BigDecimal.valueOf(price);
        bigDecimal = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP);

        return bigDecimal.doubleValue();
    }

//    public static void main(String[] args) {
//        PriceRule priceRule = new PriceRule();
//        priceRule.setUnitPricePerMile(1.8);
//        priceRule.setUnitPricePerMinute(0.5);
//        priceRule.setStartFare(10.0);
//        priceRule.setStartMile(3);
//
//        double price = getPrice(6500, 1800, priceRule);
//        System.out.println(price);
//    }
}
