package com.lyz.ServiceMap.service;


import com.lyz.ServiceMap.mapper.DicDistrictMapper;
import com.lyz.ServiceMap.remote.MapDicDistrictClient;
import com.lyz.internalcommon.constant.AmapConfigConstant;
import com.lyz.internalcommon.constant.CommonStatusEnum;
import com.lyz.internalcommon.dto.DicDistrict;
import com.lyz.internalcommon.dto.ResponseResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DistrictService {

    @Autowired
    MapDicDistrictClient mapDicDistrictClient;

    @Autowired
    DicDistrictMapper dicDistrictMapper;


    public ResponseResult initDicDistrict(String keywords){
        String dicDistrictResult = mapDicDistrictClient.dicDistrict(keywords);
    System.out.println(dicDistrictResult);

        //解析结果
        JSONObject dicDistrictJsonObject = JSONObject.fromObject(dicDistrictResult);
        int statue = dicDistrictJsonObject.getInt(AmapConfigConstant.STATUS);
        if(statue != 1){
            return ResponseResult.fail(CommonStatusEnum.MAP_DISTRICT_ERROR.getCode(),CommonStatusEnum.MAP_DISTRICT_ERROR.getValue());
        }

        JSONArray countryJsonArray = dicDistrictJsonObject.getJSONArray(AmapConfigConstant.DISTRICTS);
        for(int country = 0;country<countryJsonArray.size();country++){
            JSONObject countryJsonObject = countryJsonArray.getJSONObject(country);
            String countryAddressCode = countryJsonObject.getString(AmapConfigConstant.ADCODE);
            String countryAddressName = countryJsonObject.getString(AmapConfigConstant.NAME);
            String countryParentAddressCode = "0";
            String countryLevel = countryJsonObject.getString(AmapConfigConstant.LEVEL);
            int levelInt = generateLevel(countryLevel);

            insertDistrict(countryAddressCode,countryAddressName,levelInt,countryParentAddressCode);


            JSONArray provinceJsonArray = countryJsonObject.getJSONArray(AmapConfigConstant.DISTRICTS);
            for(int p = 0;p < provinceJsonArray.size();p++){
                JSONObject provinceJsonObject = provinceJsonArray.getJSONObject(p);
                String provinceAddressCode = provinceJsonObject.getString(AmapConfigConstant.ADCODE);
                String provinceAddressName = provinceJsonObject.getString(AmapConfigConstant.NAME);
                String provinceParentAddressCode = countryAddressCode;
                String level = provinceJsonObject.getString(AmapConfigConstant.LEVEL);
                levelInt = generateLevel(level);

                insertDistrict(provinceAddressCode,provinceAddressName,levelInt,provinceParentAddressCode);


                JSONArray cityArray = provinceJsonObject.getJSONArray(AmapConfigConstant.DISTRICTS);
                for(int city = 0;city < cityArray.size();city++) {
                    JSONObject cityJsonObject = cityArray.getJSONObject(city);
                    String cityAddressCode = cityJsonObject.getString(AmapConfigConstant.ADCODE);
                    String cityAddressName = cityJsonObject.getString(AmapConfigConstant.NAME);
                    String cityParentAddressCode = provinceAddressCode;
                    String cityLevel = cityJsonObject.getString(AmapConfigConstant.LEVEL);
                    levelInt = generateLevel(cityLevel);

                    insertDistrict(cityAddressCode, cityAddressName, levelInt, cityParentAddressCode);


                    JSONArray districtArray = cityJsonObject.getJSONArray(AmapConfigConstant.DISTRICTS);
                    for(int d = 0;d < districtArray.size();d++) {
                        JSONObject districtJsonObject = districtArray.getJSONObject(d);
                        String districtAddressCode = districtJsonObject.getString(AmapConfigConstant.ADCODE);
                        String districtAddressName = districtJsonObject.getString(AmapConfigConstant.NAME);
                        String districtParentAddressCode = cityAddressCode;
                        String districtLevel = districtJsonObject.getString(AmapConfigConstant.LEVEL);
                        levelInt = generateLevel(districtLevel);

                        if(districtLevel.equals(AmapConfigConstant.STREET)){
                            continue;
                        }

                        insertDistrict(districtAddressCode, districtAddressName, levelInt, districtParentAddressCode);

                    }
                }
            }
        }

        return ResponseResult.success();
    }

    public void insertDistrict(String addressCode,String addressName,int levelInt,String parentAddressCode){
        DicDistrict district = new DicDistrict();
        district.setAddressCode(addressCode);
        district.setAddressName(addressName);
        district.setLevel(levelInt);
        district.setParentAddressCode(parentAddressCode);
        dicDistrictMapper.insert(district);
    }

    public int generateLevel(String level){
        int levelInt = 0;
        if(level.trim().equals("country")){
            levelInt = 0;
        }else if (level.trim().equals("province")){
            levelInt = 1;
        }else if (level.trim().equals("city")){
            levelInt = 2;
        }else if (level.trim().equals("district")){
            levelInt = 3;
        }
        return levelInt;
    }




}
