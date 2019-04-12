package com.open.capacity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2018年04月23日 上午20:01:06 类说明
 */
@SpringBootApplication
public class TraditionalSimplifiedWebServiceApp {
	 
	public static void main(String[] args) {

		ConfigurableApplicationContext context =  SpringApplication.run(TraditionalSimplifiedWebServiceApp.class, args);
		
//		StringEncryptor  encryptor = context.getBean(StringEncryptor.class) ;
//		System.out.println(encryptor.encrypt("hello"));
//		System.out.println(encryptor.decrypt("I+eM0iZOi5513g0PVyqJsQ=="));
//		context.close();
	}
}

