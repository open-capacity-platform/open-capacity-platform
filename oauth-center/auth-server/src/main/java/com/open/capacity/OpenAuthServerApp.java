/**
 * 
 */
package com.open.capacity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.open.capacity.annotation.EnableLogging;
import com.open.capacity.autoconfigure.port.PortApplicationEnvironmentPreparedEventListener;

/** 
* @author owen 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51
* 类说明 
*/
@EnableLogging
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class OpenAuthServerApp {
	
	public static void main(String[] args) {
//		固定端口启动
//		SpringApplication.run(OpenAuthServerApp.class, args);
		
		//随机端口启动
		SpringApplication app = new SpringApplication(OpenAuthServerApp.class);
        app.addListeners(new PortApplicationEnvironmentPreparedEventListener());
        app.run(args);
		
	}

}
