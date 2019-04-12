package com.open.capacity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

import com.open.capacity.annotation.EnableLogging;

@Configuration
@EnableLogging
@EnableDiscoveryClient
@SpringBootApplication
public class GeneratorCenterApplication {
	public static void main(String[] args) {
		SpringApplication.run(GeneratorCenterApplication.class, args);
	}
}
