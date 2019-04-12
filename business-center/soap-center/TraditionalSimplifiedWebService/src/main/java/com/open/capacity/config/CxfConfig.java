package com.open.capacity.config;


import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.com.webxml.TraditionalSimplifiedWebServiceSoap;
import cn.com.webxml.TraditionalSimplifiedWebServiceSoapImpl;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2018年04月23日 上午20:01:06 类说明
 * cxf配置
 */

@Configuration
public class CxfConfig {
	
 
	@Bean
	public ServletRegistrationBean cxfServlet() {
		return new ServletRegistrationBean(new CXFServlet(), "/services/*");
	}

	@Bean(name = Bus.DEFAULT_BUS_ID)
	public SpringBus springBus() {
		return new SpringBus();
	}

	@Bean
	public TraditionalSimplifiedWebServiceSoap traditionalSimplifiedWebServiceSoap() {
		
		TraditionalSimplifiedWebServiceSoap traditionalSimplifiedWebServiceSoap = new TraditionalSimplifiedWebServiceSoapImpl() ;
		return traditionalSimplifiedWebServiceSoap;
	}

	@Bean
	public Endpoint endpoint() {
		EndpointImpl endpoint = new EndpointImpl(springBus(), traditionalSimplifiedWebServiceSoap());
		endpoint.publish("/traditionalSimplifiedWebServiceSoap");
		return endpoint;
	}
}
