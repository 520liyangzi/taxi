package com.lyz.internalcommon.request;

import lombok.Data;

@Data
public class VerificationCodeDTO {

    private String passengerPhone;

    private String verificationCode;

    private String driverPhone;
}

