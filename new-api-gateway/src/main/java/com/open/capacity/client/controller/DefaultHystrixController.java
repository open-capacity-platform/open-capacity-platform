package com.open.capacity.client.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 默认降级处理
 */
@RestController
public class DefaultHystrixController {

    @RequestMapping("/defaultfallback")
    public Map<String,String> defaultfallback(){
        Map<String,String> map = new LinkedHashMap<>();
        map.put("resp_code",HttpStatus.INTERNAL_SERVER_ERROR+"");
        map.put("resp_msg","服务降级");
        return map;
    }
}