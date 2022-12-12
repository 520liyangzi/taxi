package com.lyz.internalcommon.response;

import lombok.Data;

@Data
public class OrderDriverResponse {
    Long driverId;
    String driverPhone;
    Long carId;
    /**
     * 机动车驾驶证号
     */
    private String licenseId;

    /**
     * 车辆号牌
     */
    private String vehicleNo;

    /**
     * 车辆类型
     */
    private String vehicleType;
}
