package com.open.capacity;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;

public class Test {
	
	@Rule
	public ContiPerfRule i = new ContiPerfRule();
	
//	private static final String BASE_URL="http://134.224.249.39:9200/api-auth/oauth/userinfo";
 
//	private static final String BASE_URL="http://134.224.249.39:8000/oauth/userinfo";
	 
	private static final String BASE_URL="http://apis.juhe.cn/mobile/get?phone=18579068155&dtype=json&key=d84d4da6884d2ecb833eb9a46feca30d";
	
	@org.junit.Test
	@PerfTest(invocations = 10000,threads = 100)
	public void test() throws ClientProtocolException, IOException {
		
		CloseableHttpClient httpClient  = HttpClients.createDefault();
		HttpGet httpGet =  new HttpGet(BASE_URL);
		httpGet.addHeader("Authorization", "Bearer " + "521d52b4-9069-4c15-80e9-0d735983aaf2");
		CloseableHttpResponse response = null ;
		try {
			// 执行请求
			response = httpClient.execute(httpGet);
			// 判断返回状态是否为200

			String content = EntityUtils.toString(response.getEntity(), "UTF-8");

//			System.out.println(content);
		} finally {
			if (response != null) {
				response.close();
			}
			httpClient.close();
		}
		
	}
	 
	
}
