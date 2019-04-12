package com.open.capacity.client.filter;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * * 程序名 : AccessFilter
 * 建立日期: 2018-09-09
 * 作者 : someday
 * 模块 : 网关
 * 描述 : oauth校验
 * 备注 : version20180909001
 * <p>
 * 修改历史
 * 序号 	       日期 		        修改人 		         修改原因
 */
@Component
public class AccessFilter implements GlobalFilter ,Ordered{
	
	// url匹配器
	private AntPathMatcher pathMatcher = new AntPathMatcher();

	@Resource
	private RedisTemplate<String, Object> redisTemplate ;
	
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return -500;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// TODO Auto-generated method stub
		
		String accessToken = extractToken(exchange.getRequest());
		
		
		if(pathMatcher.match("/**/v2/api-docs/**",exchange.getRequest().getPath().value())){
			return chain.filter(exchange);
		}
		
		if(!pathMatcher.match("/api-auth/**",exchange.getRequest().getPath().value())){
			if (accessToken == null) {
				exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				return exchange.getResponse().setComplete();
			}else{
				try {
					Map<String, Object> params =  (Map<String, Object>) redisTemplate.opsForValue().get("token:" + accessToken) ;
					if(params.isEmpty()){
						exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
						return exchange.getResponse().setComplete();
					}
				} catch (Exception e) {
					exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
					return exchange.getResponse().setComplete();
				} 
			}
		}
		
		
		
		return chain.filter(exchange);
	}

	protected String extractToken(ServerHttpRequest request) {
		List<String> strings = request.getHeaders().get("Authorization");
        String authToken = null;
        if (strings != null) {
            authToken = strings.get(0).substring("Bearer".length()).trim();
        }
        
        
        if (StringUtils.isBlank(authToken)) {
            strings = request.getQueryParams().get("access_token");
            if (strings != null) {
                authToken = strings.get(0);
            }
        }
		
		return authToken;
	}
	
}
