//package com.open.capacity.config;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
//import org.springframework.cloud.client.discovery.DiscoveryClient;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
//import org.springframework.stereotype.Component;
//
//@Configuration
//public class OAuth2SSOConfig {
//
//	
//	@Component
//	public static class OAuth2ProtectedResourceDetailsProcessor implements BeanPostProcessor {
//
//		@Autowired
//		private DiscoveryClient discoveryClient;
//		
//		@Override
//		public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//			// 置换代理对象
//			if (bean instanceof AuthorizationCodeResourceDetails) {
//
//				AuthorizationCodeResourceDetails obj = (AuthorizationCodeResourceDetails) bean;
//
//				
//				obj.setUserAuthorizationUri(discoveryClient.getInstances("auth-server").iterator().next().getUri() +"/oauth/authorize");	
//				obj.setAccessTokenUri(discoveryClient.getInstances("auth-server").iterator().next().getUri() +"/oauth/token");
//				
//				System.out.println(obj);
//
//				return bean;
//			}
//			if (bean instanceof ResourceServerProperties ){
//				
//				ResourceServerProperties obj = (ResourceServerProperties) bean;
//				obj.setTokenInfoUri(discoveryClient.getInstances("auth-server").iterator().next().getUri() +"/oauth/check_token");
//				return obj ;
//				
//			}
//
//			return bean;
//		}
//
//		@Override
//		public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//			return bean;
//		}
//
//	}
//
//}
