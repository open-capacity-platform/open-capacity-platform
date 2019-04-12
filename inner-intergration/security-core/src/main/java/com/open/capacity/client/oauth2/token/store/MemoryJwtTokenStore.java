package com.open.capacity.client.oauth2.token.store;
///*
// * Copyright 2013-2014 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
// * the License. You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
// * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
// * specific language governing permissions andists anything, and methods like {@link #getAccessToken(OAuth2Authentication)} always return null. But
// * nevertheless a useful tool since it translates access tokens to and from authentications. Use this wherever a
// * {@link TokenStore}
//
// limitations under the License.
// */
//
//package com.open.capacity.server.oauth2.token.store;
//
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//
//import com.open.capacity.model.system.LoginAppUser;
//import com.open.capacity.model.system.SysUser;
//
// 
//public class MemoryJwtTokenStore extends JwtTokenStore {
//	public MemoryJwtTokenStore(JwtAccessTokenConverter jwtTokenEnhancer) {
//		super(jwtTokenEnhancer);
//		// TODO Auto-generated constructor stub
//	}
//	@Override
//	public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
//		
//		LoginAppUser user = (LoginAppUser) authentication.getPrincipal() ;
//		
//		token.getAdditionalInformation().put("user", user ) ; 
//		
//	}
//	 
//}
