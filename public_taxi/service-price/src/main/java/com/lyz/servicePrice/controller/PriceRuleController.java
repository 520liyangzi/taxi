package com.lyz.servicePrice.controller;


import com.lyz.internalcommon.dto.PriceRule;
import com.lyz.internalcommon.dto.ResponseResult;
import com.lyz.servicePrice.service.ForecastService;
import com.lyz.servicePrice.service.PriceRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Yangzi
 * @since 2022-12-06
 */
@RestController
@RequestMapping("/price-rule")
public class PriceRuleController {
    @Autowired
    PriceRuleService priceRuleService;

    @Autowired
    ForecastService forecastService;

    @PostMapping("/add")
    public ResponseResult add(@RequestBody PriceRule priceRule){
        return priceRuleService.add(priceRule);
    }

    @PostMapping("/edit")
    public ResponseResult edit(@RequestBody PriceRule priceRule){
        return priceRuleService.edit(priceRule);
    }

    @PostMapping("/if-exists")
    public ResponseResult<Boolean> ifExists(@RequestBody PriceRule priceRule){
        System.out.println(priceRule);
        return priceRuleService.ifExists(priceRule);
    }
}
