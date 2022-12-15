package com.lyz.internalcommon.constant;

import lombok.Data;
import lombok.Getter;


public enum CommonStatusEnum {

    VERIFICATION_CODE_ERROR(1099,"验证码不正确"),


    TOKEN_ERROR(1199,"token错误"),

    USER_NOT_EXISTS(1200,"用户不存在"),

    PRICE_RULE_EMPTY(1300,"规则不存在"),

    MAP_DISTRICT_ERROR(1400,"请求地图错误"),

    DRIVER_CAR_BIND_EXISTS(1500,"关系存在，请勿重新绑定"),

    DRIVER_CAR_BIND_EMPTY(1600,"不存在绑定关系，无法解绑"),

    DRIVER_NOT_EXISTS(1601,"司机不存在"),

    PRICE_RULE_EXISTS(1602,"计划规则存在"),

    PRICE_RULE_NOT_EDIT(1603,"规则没改变"),

    ORDER_NOT_CREATE(1604,"不允许创建"),

    BLACK(1605,"黑名单"),

    NO_DRIVER(1606,"没有可用的司机"),

    AVAILIABLE_DRIVER_EMPTY(1607,"可用的司机唯恐"),

    ORDER_CANCEL_ERROR(1608,"取消失败"),





    /**
     * 1成功
     * 0失败
     */

    SUCCESS(1,"success"),
    FAIL(0,"fail")
    ;
    @Getter
    private int code;


    @Getter
    private String value;

    CommonStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

}
