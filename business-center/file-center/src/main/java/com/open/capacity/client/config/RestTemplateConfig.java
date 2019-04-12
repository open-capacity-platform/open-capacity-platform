package com.open.capacity.client.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;


/**
 * @author 作者 owen E-mail: 624191343@qq.com
* @version 创建时间：2017年11月12日 上午22:57:51
 * RestTemplate高并发下异常与配置说明
 * 1、java.util.ConcurrentModificationException
 * 2、java.net.SocketTimeoutException Connection timed out
 */
@Configuration
public class RestTemplateConfig {

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
//		 长连接
		PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager();
//		 总连接数
		pollingConnectionManager.setMaxTotal(1000);
//		同路由的并发数
		pollingConnectionManager.setDefaultMaxPerRoute(1000);

		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		httpClientBuilder.setConnectionManager(pollingConnectionManager);
//		  重试次数，默认是3次，没有开启
//		httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(3, true));
		HttpClient httpClient = httpClientBuilder.build();

		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
				httpClient);
//		 连接超时
		clientHttpRequestFactory.setConnectTimeout(12000);
//		 数据读取超时时间，即SocketTimeout
		clientHttpRequestFactory.setReadTimeout(12000);
//		连接不够用的等待时间，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的
		clientHttpRequestFactory.setConnectionRequestTimeout(200);
//		缓冲请求数据，默认值是true。通过POST或者PUT大量发送数据时，建议将此属性更改为false，以免耗尽内存。
//		clientHttpRequestFactory.setBufferRequestBody(false);

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(clientHttpRequestFactory);
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler());

		return restTemplate;
	}
}
