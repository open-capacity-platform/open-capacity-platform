package com.open.capacity.oss.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aliyun.oss.OSSClient;

 
/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2018年1月31日 下午9:11:36 类说明 白名单
 * 阿里云配置
 */
@Configuration
public class AliyunOSSConfig {

	@Value("${aliyun.oss.endpoint:xxxxx}")
	private String endpoint;
	@Value("${aliyun.oss.access-key:xxxxx}")
	private String accessKeyId;
	@Value("${aliyun.oss.accessKeySecret:xxxxx}")
	private String accessKeySecret;

	/**
	 * 阿里云文件存储client
	 * 只有配置了aliyun.oss.access-key才可以使用
	 */
	@Bean
	@ConditionalOnProperty(name = "aliyun.oss.access-key", matchIfMissing = true)
	public OSSClient ossClient() {
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		return ossClient;
	}

}
