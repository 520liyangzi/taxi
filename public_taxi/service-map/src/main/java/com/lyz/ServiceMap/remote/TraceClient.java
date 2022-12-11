package com.lyz.ServiceMap.remote;

import com.lyz.internalcommon.constant.AmapConfigConstant;
import com.lyz.internalcommon.dto.ResponseResult;

import com.lyz.internalcommon.dto.TraceResponse;
import com.lyz.internalcommon.response.TerminalResponse;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TraceClient {

    @Value("${amap.key}")
    private String key;

    @Value("${amap.sid}")
    private String sid;

    @Autowired
    RestTemplate restTemplate;
    public ResponseResult<TerminalResponse> add(String tid){
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstant.TRACE_ADD_URL);
        url.append("?");
        url.append("key="+key);
        url.append("&");
        url.append("sid="+sid);
        url.append("&");
        url.append("tid="+tid);
        ResponseEntity<String> forEntity = restTemplate.postForEntity(url.toString(), null,String.class);
        String body = forEntity.getBody();
        JSONObject result = JSONObject.fromObject(body);
        System.out.println(result);
        JSONObject data = result.getJSONObject("data");
        System.out.println(data);
        String trid = data.getString("trid");
        String trname = "";
        if(data.has("trname")){
            trname = data.getString("trname");
        }
        TraceResponse traceRespose = new TraceResponse();
        traceRespose.setTrid(trid);
        traceRespose.setTrname(trname);
        return ResponseResult.success(traceRespose);
    }
}
