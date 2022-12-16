package com.lyz.serviceDriverUser.controller;

import com.lyz.internalcommon.dto.DriverUser;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.internalcommon.dto.TokenResult;
import com.lyz.internalcommon.response.DriverUserExistsResponse;
import com.lyz.internalcommon.response.OrderDriverResponse;
import com.lyz.internalcommon.util.JwtUtils;
import com.lyz.serviceDriverUser.service.DriverUserService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private DriverUserService driverUserService;

    /**
     * 新增
     * @param driverUser
     * @return
     */
    @PostMapping("/user")
    public ResponseResult addDriverUser(@RequestBody DriverUser driverUser){
        return driverUserService.addDriverUser(driverUser);
    }
    /**
     * 修改
     * @param driverUser
     * @return
     */
    @PutMapping("/user")
    public ResponseResult updateDriverUser(@RequestBody DriverUser driverUser){
        log.info(JSONObject.fromObject(driverUser).toString());
        return driverUserService.updateDriverUser(driverUser);
    }

    /**
     * 查询
     * @param
     * @return
     */
    @GetMapping("/check-driver/{driverPhone}")
    public ResponseResult<DriverUserExistsResponse> getUser(@PathVariable("driverPhone")String driverPhone){
        ResponseResult<DriverUser> driverUserByPhone = driverUserService.getDriverUser(driverPhone);
        DriverUser data = driverUserByPhone.getData();
        DriverUserExistsResponse response = new DriverUserExistsResponse();
        int isExits = 1;
        if(data == null) {
            isExits = 0;
        }else {
            response.setDriverPhone(data.getDriverPhone());
        }
        response.setIsExists(isExits);
        return ResponseResult.success(response);
    }

    @GetMapping("/get-availiable-driver/{carId}")
    public ResponseResult<OrderDriverResponse> getAvailiableDriver(@PathVariable("carId") Long carId){
        return driverUserService.getAvailiableDriver(carId);
    }

    /**
     * 根据司机手机号查询司机和车辆绑定关系
     * @param driverPhone
     * @return
     */
    @GetMapping("/driver-car- binding-relationship")
    public ResponseResult getCarBindingByDriverId(@RequestParam String driverPhone){
        return driverUserService.getCarBindingByDriverId(driverPhone);
    }
}
