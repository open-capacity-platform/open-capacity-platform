package com.open.capacity.server.oauth2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.open.capacity.model.system.LoginAppUser;
import com.open.capacity.server.oauth2.feign.UserClient;
import com.open.capacity.server.oauth2.feign.UserLoginGrpc;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserClient userClient;
    
    @Autowired
    private UserLoginGrpc userLoginGrpc;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//      后续考虑集成spring socail,支持多种类型登录
        LoginAppUser loginAppUser = userClient.findByUsername(username);   			  //方式1  feign调用       对外feign resttemplate
        
//        LoginAppUser loginAppUser = userLoginGrpc.findByUsername(username);		  //方式2  gprc调用		对内grpc dubbo
        if (loginAppUser == null) {
            throw new AuthenticationCredentialsNotFoundException("用户不存在");
        } else if (!loginAppUser.isEnabled()) {
            throw new DisabledException("用户已作废");
        }

        return loginAppUser;
    }


     
}
