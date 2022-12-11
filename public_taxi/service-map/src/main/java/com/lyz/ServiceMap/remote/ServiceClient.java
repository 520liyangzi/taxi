package com.lyz.ServiceMap.remote;

import com.lyz.internalcommon.constant.AmapConfigConstant;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.dto.ServiceResponse;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ServiceClient {


    @Value("${amap.key}")
    private String key;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseResult add(String name){
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstant.SERVICE_ADD_URL);
        url.append("?");
        url.append("key="+key);
        url.append("&");
        url.append("name="+name);
        ResponseEntity<String> forEntity = restTemplate.postForEntity(url.toString(), null,String.class);
        System.out.println(url.toString());
        String body = forEntity.getBody();
        JSONObject result = JSONObject.fromObject(body);
        JSONObject data = result.getJSONObject("data");
        System.out.println("data     " +   data);
        String sid = data.getString("sid");
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setSid(sid);
        return ResponseResult.success(serviceResponse);
    }

}
