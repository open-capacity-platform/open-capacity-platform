package com.open.capacity.generator.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.open.capacity.commons.PageResult;
import com.open.capacity.generator.service.SysGeneratorService;
import com.open.capacity.log.monitor.PointUtil;

import io.swagger.annotations.Api;

/**
 * @Author: [zhangzhiguang]
 * @Date: [2018-09-05 12:11]
 * @Description: [ ]
 * @Version: [1.0.0]
 * @Copy: [com.zzg]
 */
@RestController
@Api(tags = "代码生成器")
@RequestMapping("/generator")
public class SysGeneratorController {
    @Autowired
    private SysGeneratorService sysGeneratorService;
    
    private static Logger log = LoggerFactory.getLogger(SysGeneratorController.class);
	private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 列表
     * @throws JsonProcessingException 
     */
    @ResponseBody
    @RequestMapping("/list")
    public PageResult list(@RequestParam Map<String, Object> params) throws JsonProcessingException{
    	
        return sysGeneratorService.queryList(params);
    }

    /**
     * 生成代码
     */
    @RequestMapping("/code")
    public void code(String tables, HttpServletResponse response) throws IOException {
    	
    	
        byte[] data = sysGeneratorService.generatorCode(tables.split(","));
        
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"generator.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }


}
