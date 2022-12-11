package com.lyz.internalcommon.request;

import lombok.Data;

@Data
public class ApiDriverPointRequest {
    private Long carId;

    private PointDTO[] points;
}
