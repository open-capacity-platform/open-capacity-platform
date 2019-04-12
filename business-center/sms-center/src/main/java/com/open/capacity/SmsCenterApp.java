package com.open.capacity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 短信中心
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
public class SmsCenterApp {

	public static void main(String[] args) {
		SpringApplication.run(SmsCenterApp.class, args);
	}

}