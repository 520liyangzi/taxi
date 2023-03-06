package com.lyz.ServiceMap.remote;

import com.lyz.internalcommon.constant.AmapConfigConstant;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.dto.ServiceResponse;

import com.lyz.internalcommon.response.TerminalResponse;
import com.lyz.internalcommon.response.TrearchResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class TerminalClient {

    @Value("${amap.key}")
    private String key;

    @Value("${amap.sid}")
    private String sid;

    @Autowired
    RestTemplate restTemplate;
    public ResponseResult<TerminalResponse> add(String name,String desc){
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstant.TERMINAL_ADD_URL);
        url.append("?");
        url.append("key="+key);
        url.append("&");
        url.append("sid="+sid);
        url.append("&");
        url.append("name="+name);
        url.append("&");
        url.append("desc="+desc);
        ResponseEntity<String> forEntity = restTemplate.postForEntity(url.toString(), null,String.class);
        String body = forEntity.getBody();
        JSONObject result = JSONObject.fromObject(body);
        System.out.println(result);
        JSONObject data = result.getJSONObject("data");
        System.out.println(data);
        String tid = data.getString("tid");
        TerminalResponse terminalRespose = new TerminalResponse();
        terminalRespose.setTid(tid);
        return ResponseResult.success(terminalRespose);
    }


    public ResponseResult<List<TerminalResponse>> aroundsearch(String center, Integer radius){
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstant.AROUND_SEARCH);
        url.append("?");
        url.append("key="+key);
        url.append("&");
        url.append("sid="+sid);
        url.append("&");
        url.append("center="+center);
        url.append("&");
        url.append("radius="+radius);
        System.out.println("请求   "+url.toString());
        ResponseEntity<String> forEntity = restTemplate.postForEntity(url.toString(), null,String.class);
        System.out.println("相应  "+forEntity);

        String body = forEntity.getBody();
        JSONObject jsonObject = JSONObject.fromObject(body);
        JSONObject data = jsonObject.getJSONObject("data");
        if (data == null){
            return ResponseResult.fail("没有结果");
        }
        JSONArray results = data.getJSONArray("results");
        List<TerminalResponse> list = new ArrayList<>();
        for(int i = 0;i<results.size();i++){
            JSONObject jsonobj = results.getJSONObject(i);
            System.out.println("qqqqqqq" + jsonobj);
            //desc转为carId
            String desc = jsonobj.getString("desc");
            Long carId = Long.parseLong(desc);
            //tid为终端信息
            String tid = jsonobj.getString("tid");
            //现在要写上精度和纬度信息
            JSONObject location = jsonobj.getJSONObject("location");
            String longitudeString = location.getString("longitude");
            String latitudeString = location.getString("latitude");
            TerminalResponse terminalRespose = new TerminalResponse();
            terminalRespose.setLongitude(longitudeString);
            terminalRespose.setLatitude(latitudeString);
            terminalRespose.setTid(tid);
            terminalRespose.setCarId(carId);
            list.add(terminalRespose);
        }
        return ResponseResult.success(list);
    }

    /**
     * 获得路程和时长
     * @param tid
     * @param startTime
     * @param endTime
     * @return
     */
    public ResponseResult<TrearchResponse> trsearch(String tid, Long startTime, Long endTime) {
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigConstant.TRSEARCH);
        url.append("?");
        url.append("key="+key);
        url.append("&");
        url.append("sid="+sid);
        url.append("&");
        url.append("tid="+tid);
        url.append("&");
        url.append("starttime="+startTime);
        url.append("&");
        url.append("endtime="+endTime);
        System.out.println("请求   "+url.toString());
        ResponseEntity<String> forEntity = restTemplate.postForEntity(url.toString(), null,String.class);
        System.out.println("相应  "+forEntity);
        JSONObject result = JSONObject.fromObject(forEntity.getBody());
        JSONObject data = result.getJSONObject("data");
        int counts = data.getInt("counts");
        if(counts == 0){
            return ResponseResult.fail("没有记录");
        }
        JSONArray tracks = data.getJSONArray("tracks");
        long driveMile = 0L;
        long driveTime = 0L;
        for(int i = 0;i<tracks.size();i++){
            JSONObject jsonObject = tracks.getJSONObject(i);

            long distance = jsonObject.getLong("distance");
            driveMile += distance;

            long time = jsonObject.getLong("time");
            time = time / (1000 * 60);
            driveTime += time;
        }
        TrearchResponse trearchResponse = new TrearchResponse();
        trearchResponse.setDriveMile(driveMile);
        trearchResponse.setDriveTime(driveTime);

        return ResponseResult.success(trearchResponse);
    }
}
