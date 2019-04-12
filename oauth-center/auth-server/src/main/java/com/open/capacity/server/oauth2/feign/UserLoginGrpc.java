package com.open.capacity.server.oauth2.feign;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.open.capacity.grpc.lib.BizGrpc;
import com.open.capacity.grpc.lib.BizOuterClass;
import com.open.capacity.model.system.LoginAppUser;

import io.grpc.Channel;
import net.devh.springboot.autoconfigure.grpc.client.GrpcClient;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51 调用用户中心中的userdetail对象，用户oauth中的登录
 *          获取的用户与页面输入的密码 进行BCryptPasswordEncoder匹配
 */
@Service
public class UserLoginGrpc {

	@GrpcClient("user-center")  //grpc客户端
	private Channel serverChannel;
	@Resource
	private ObjectMapper objectMapper; 

	public LoginAppUser findByUsername(String name) {

		LoginAppUser user = null ;
		BizGrpc.BizBlockingStub stub = BizGrpc.newBlockingStub(serverChannel);
		BizOuterClass.GprcReply response = stub.process(BizOuterClass.GprcRequest.newBuilder().setName(name).build());
		
		try {
			user = objectMapper.readValue(response.getMessage(), LoginAppUser.class) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user ; 

	}

}
