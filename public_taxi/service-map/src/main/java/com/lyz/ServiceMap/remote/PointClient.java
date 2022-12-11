package com.lyz.ServiceMap.remote;

import com.lyz.internalcommon.constant.AmapConfigConstant;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.dto.TraceResponse;
import com.lyz.internalcommon.request.PointDTO;
import com.lyz.internalcommon.request.PointRequest;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class PointClient {

    @Value("${amap.key}")
    private String key;

    @Value("${amap.sid}")
    private String sid;

    @Autowired
    RestTemplate restTemplate;

    public ResponseResult upload(PointRequest pointRequest){

        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstant.POINT_UPLOAD);
        url.append("?");
        url.append("key="+key);
        url.append("&");
        url.append("sid="+sid);
        url.append("&");
        url.append("tid="+pointRequest.getTid());
        url.append("&");
        url.append("trid="+pointRequest.getTrid());
        url.append("&");
        url.append("points=");
        url.append("%5B");
        // [   %5B
        PointDTO[] points = pointRequest.getPoints();
        for(PointDTO p : points){
            url.append("%7B");
            // {    %7B
            String location = p.getLocation();
            String locatetime = p.getLocatetime();
            //  " %22
            url.append("%22location%22");
            // : %3A
            url.append("%3A");
            url.append("%22"+location+"%22");
            //  ,  %2C
            url.append("%2C");
            url.append("%22locatetime%22");
            url.append("%3A");
            url.append(locatetime);
            url.append("%7D");
            // |    %7D
        }
        url.append("%5D");
            // [   %5D
        System.out.println(url.toString());
        ResponseEntity<String> forEntity = restTemplate.postForEntity(URI.create(url.toString()), null,String.class);
        String body = forEntity.getBody();
        System.out.println(body);
        return ResponseResult.success();
//        JSONObject result = JSONObject.fromObject(body);
//        System.out.println(result);
//        return ResponseResult.success(result);
//        JSONObject data = result.getJSONObject("data");
//        System.out.println(data);
//        String trid = data.getString("trid");
//        String trname = "";
//        if(data.has("trname")){
//            trname = data.getString("trname");
//        }
//        TraceResponse traceRespose = new TraceResponse();
//        traceRespose.setTrid(trid);
//        traceRespose.setTrname(trname);

    }

}
