package com.lyz.ServiceMap.remote;


import com.lyz.internalcommon.constant.AmapConfigConstant;
import com.lyz.internalcommon.response.DirectionResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class MapDirectionClient {
    @Value("${amap.key}")
    private String amapKey;

    @Autowired
   private RestTemplate restTemplate;

    public DirectionResponse direction(String depLongitude, String depLatitude, String destLongitude, String destLatitude){
        //组装调用URL

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(AmapConfigConstant.DIRECTION_URL);
        urlBuilder.append("?");
        urlBuilder.append("origin="+depLongitude+","+depLatitude);
        urlBuilder.append("&");
        urlBuilder.append("destination="+destLongitude+","+destLatitude);
        urlBuilder.append("&");
        urlBuilder.append("extensions=base&output=json");
        urlBuilder.append("&");
        urlBuilder.append("key="+amapKey);

        log.info(urlBuilder.toString());
        //调用高德接口

        //解析接口
        ResponseEntity<String> directionForEntity = restTemplate.getForEntity(urlBuilder.toString(), String.class);
        String directionString = directionForEntity.getBody();
       // log.info("历经规划"+directionString);
        DirectionResponse directionResponse = parseDirectionEntity(directionString);
        return directionResponse;
    }

    private DirectionResponse parseDirectionEntity(String directionString){
        DirectionResponse directionResponse = null;
        try {
            JSONObject result = JSONObject.fromObject(directionString);
            if (result.has(AmapConfigConstant.STATUS)){
                int status = result.getInt(AmapConfigConstant.STATUS);
                if (status == 1){
                    if(result.has(AmapConfigConstant.ROUTE)){
                        JSONObject routeObject = result.getJSONObject(AmapConfigConstant.ROUTE);
                        JSONArray pathsArray = routeObject.getJSONArray(AmapConfigConstant.PATHS);
                        JSONObject pathObject = pathsArray.getJSONObject(0);
                        directionResponse = new DirectionResponse();
                        if(pathObject.has(AmapConfigConstant.DISTANCE) && pathObject.has(AmapConfigConstant.DURATION)){
                            int distance = pathObject.getInt(AmapConfigConstant.DISTANCE);
                            int duration = pathObject.getInt(AmapConfigConstant.DURATION);
                            directionResponse.setDuraction(duration);
                            directionResponse.setDistance(distance);
                        }
                    }
                }
            }

        }catch (Exception e){

        }
        return directionResponse;
    }
}
