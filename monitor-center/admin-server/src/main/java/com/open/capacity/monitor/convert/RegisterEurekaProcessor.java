//package com.open.capacity.monitor.convert;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.stereotype.Component;
//
//import de.codecentric.boot.admin.server.cloud.discovery.EurekaServiceInstanceConverter;
//
///**
// * @author 作者 owen E-mail: 624191343@qq.com
// * @version 创建时间：2017年11月24日 下午5:34:47 类说明
// * 偷梁换柱 将druid api 注册到监控中心
// */
//@Component
//public class RegisterEurekaProcessor implements BeanPostProcessor{
//
//	@Override
//	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//		// 置换代理对象
//		if(bean instanceof EurekaServiceInstanceConverter){
//			return new EurekaConverter();
//		}
//		
//		return bean;
//	}
//
//	@Override
//	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//		return bean;
//	}
//
//	
//}
