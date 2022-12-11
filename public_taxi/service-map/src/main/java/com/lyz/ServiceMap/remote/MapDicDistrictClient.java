package com.lyz.ServiceMap.remote;

import com.lyz.internalcommon.constant.AmapConfigConstant;
import com.lyz.internalcommon.dto.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class MapDicDistrictClient {
    //对于地图的请求  都放在Clint里面

    @Value("${amap.key}")
    private String key;

    @Autowired
    private RestTemplate restTemplate;

    public String dicDistrict(String keywords){
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstant.DISTRICT_URL);
        url.append("?");
        url.append("keywords="+keywords);
        url.append("&");
        url.append("subdistrict=3");
        url.append("&");
        url.append("key="+key);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url.toString(), String.class);
        return forEntity.getBody();
    }

    //解析结果



    //插入数据库


}








