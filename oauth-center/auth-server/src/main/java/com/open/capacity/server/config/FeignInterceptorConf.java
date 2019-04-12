package com.open.capacity.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51
 * feign拦截器
 */
@Configuration
public class FeignInterceptorConf {

	/**
	 * 使用feign client访问别的微服务时，将access_token放入参数或者header ，Authorization:Bearer xxx
	 * 或者url?access_token=xxx
	 */
	@Bean
	public RequestInterceptor requestInterceptor() {
		RequestInterceptor requestInterceptor = new RequestInterceptor() {

			@Override
			public void apply(RequestTemplate template) {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (authentication != null) {
					if (authentication instanceof OAuth2Authentication) {
						OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
						String access_token = details.getTokenValue();
						template.header("Authorization", OAuth2AccessToken.BEARER_TYPE + " " + access_token);
					}

				}
			}
		};

		return requestInterceptor;
	}
}
