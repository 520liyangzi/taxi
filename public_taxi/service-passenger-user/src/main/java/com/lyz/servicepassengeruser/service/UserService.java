package com.lyz.servicepassengeruser.service;
import com.lyz.internalcommon.constant.CommonStatusEnum;
import com.lyz.internalcommon.dto.PassengerUser;
import com.lyz.internalcommon.dto.ResponseResult;

import com.lyz.servicepassengeruser.mapper.PassengerUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    PassengerUserMapper passengerUserMapper;
    public ResponseResult loginOrRegister(String passengerPhone){
        System.out.println("user serice 被diaoyong");
        //根据手机号查询用户信息
        Map<String,Object> map = new HashMap<>();
        map.put(("passenger_phone"),passengerPhone);
        List<PassengerUser> passengerUsers = passengerUserMapper.selectByMap(map);
        System.out.println(passengerUsers.size()==0?"无记录 那就插入":passengerUsers.get(0).getProfilePhoto());
        //判断用户信息是否存在

        if(passengerUsers.size() == 0){
            PassengerUser passengerUser = new PassengerUser();
            passengerUser.setId(null);
            passengerUser.setPassengerName("张张");
            passengerUser.setPassengerGender((byte) 0);
            passengerUser.setState((byte)0);
            passengerUser.setPassengerPhone(passengerPhone);
            LocalDateTime now = LocalDateTime.now();
            passengerUser.setGmtModified(now);
            passengerUser.setGmtCreate(now);
            System.out.println("我他妈直接插入");
            passengerUserMapper.insert(passengerUser);
        }
        //如果不存在  插入用户信息
        return ResponseResult.success(passengerPhone);
    }

    public ResponseResult getUserByPhone(String passengerPhone){
        Map<String,Object> map = new HashMap<>();
        map.put(("passenger_phone"),passengerPhone);
        List<PassengerUser> passengerUsers = passengerUserMapper.selectByMap(map);
        if(passengerUsers.size() == 0){
            return ResponseResult.fail(CommonStatusEnum.USER_NOT_EXISTS.getCode(),CommonStatusEnum.USER_NOT_EXISTS.getValue());
        }else {
            PassengerUser passengerUser = passengerUsers.get(0);
            return ResponseResult.success(passengerUser);
        }
    }
}
