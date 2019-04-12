package com.open.capacity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51
 */


@RestController
@EnableDiscoveryClient
@SpringBootApplication
public class SsoClientApp {

    public static void main(String[] args) {
        SpringApplication.run(SsoClientApp.class, args);
    }

   
}
