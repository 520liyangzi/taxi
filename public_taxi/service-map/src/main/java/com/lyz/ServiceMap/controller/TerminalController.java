package com.lyz.ServiceMap.controller;

import com.lyz.ServiceMap.service.TerminalService;
import com.lyz.internalcommon.dto.ResponseResult;

import com.lyz.internalcommon.response.TerminalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/terminal")
public class TerminalController {
    @Autowired
    TerminalService terminalService;

    @PostMapping("/add")
    public ResponseResult<TerminalResponse> add(String name,String desc){
        return terminalService.add(name,desc);
    }

    @PostMapping("/aroundsearch")
    public ResponseResult<List<TerminalResponse>> aroundSearch(String center , Integer radius){
        System.out.println(center + " ......" + radius);
        return terminalService.aroundSearch(center,radius);
    }

}
