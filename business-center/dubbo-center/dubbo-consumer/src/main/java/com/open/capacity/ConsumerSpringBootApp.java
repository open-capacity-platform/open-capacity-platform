package com.open.capacity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;


@SpringBootApplication
@EnableDubboConfiguration
public class ConsumerSpringBootApp {

	 public static void main(String[] args) {
		 
		SpringApplication.run(ConsumerSpringBootApp.class, args);
	}
}
