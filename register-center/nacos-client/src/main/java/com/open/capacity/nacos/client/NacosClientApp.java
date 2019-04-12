package com.open.capacity.nacos.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@EnableDiscoveryClient
@SpringBootApplication
public class NacosClientApp {

    public static void main(String[] args) {
        SpringApplication.run(NacosClientApp.class, args);
    }
    /**
     * 使用ribbon负载均衡器，用于服务提供商的负载均衡
     * @return
     */
    @Bean
	@LoadBalanced
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
