package com.open.capacity.nacos.client.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51
 */
@RestController
public class TestController {
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	

	@Resource
	private RestTemplate restTemplate ;
	 
	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}
	@GetMapping("/test")
	public String hello1() {
		
		ResponseEntity<String> result = restTemplate.getForEntity ("http://nacos-client/hello", String.class);
		return result.getBody();
	}
	 

}
