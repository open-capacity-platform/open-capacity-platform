package com.open.capacity.oauth.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51
 */
@RestController
public class TestController {
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@Resource
	private RedisTemplate< String, Object> redisTemplate ;
	 
	@Resource
	private ObjectMapper objectMapper; // springmvc启动时自动装配json处理类

	@GetMapping("/test111")
	public String hello() {
		return "hello";
	}

	 
}
