package com.open.capacity.oauth.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.open.capacity.annotation.log.LogAnnotation;
import com.open.capacity.commons.PageResult;
import com.open.capacity.model.system.LoginAppUser;
import com.open.capacity.model.system.SysPermission;
import com.open.capacity.server.oauth2.client.RedisClientDetailsService;
import com.open.capacity.utils.SpringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2018年4月28日 下午2:18:54 类说明
 */

@Api(tags = "OAuth2相关操作")
@RestController
public class OAuth2Controller {

	private static final Logger logger = LoggerFactory.getLogger(OAuth2Controller.class);
	@Resource
	private ObjectMapper objectMapper; // springmvc启动时自动装配json处理类
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenStore tokenStore;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@ApiOperation(value = "用户名密码获取token")
	@PostMapping("/oauth/user/token")
	public void getUserTokenInfo(
			@ApiParam(required = true, name = "username", value = "账号") @RequestParam(value = "username") String username,
			@ApiParam(required = true, name = "password", value = "密码") @RequestParam(value = "password") String password,
			HttpServletRequest request, HttpServletResponse response) {
		String clientId = request.getHeader("client_id");
		String clientSecret = request.getHeader("client_secret");

		try {

			if (clientId == null || "".equals(clientId)) {
				throw new UnapprovedClientAuthenticationException("请求头中无client_id信息");
			}

			if (clientSecret == null || "".equals(clientSecret)) {
				throw new UnapprovedClientAuthenticationException("请求头中无client_secret信息");
			}

			RedisClientDetailsService clientDetailsService = SpringUtil.getBean(RedisClientDetailsService.class);

			ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

			if (clientDetails == null) {
				throw new UnapprovedClientAuthenticationException("clientId对应的信息不存在");
			} else if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
				throw new UnapprovedClientAuthenticationException("clientSecret不匹配");
			}

			TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientId, clientDetails.getScope(),
					"customer");

			OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

			AuthenticationManager authenticationManager = SpringUtil.getBean(AuthenticationManager.class);

			Authentication authentication = authenticationManager.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

			AuthorizationServerTokenServices authorizationServerTokenServices = SpringUtil
					.getBean("defaultAuthorizationServerTokenServices", AuthorizationServerTokenServices.class);

			OAuth2AccessToken oAuth2AccessToken = authorizationServerTokenServices
					.createAccessToken(oAuth2Authentication);

			oAuth2Authentication.setAuthenticated(true);

			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(oAuth2AccessToken));
			response.getWriter().flush();
			response.getWriter().close();

		} catch (Exception e) {

			response.setStatus(HttpStatus.UNAUTHORIZED.value());

			response.setContentType("application/json;charset=UTF-8");

			Map<String, String> rsp = new HashMap<>();
			rsp.put("resp_code", HttpStatus.UNAUTHORIZED.value() + "");
			rsp.put("rsp_msg", e.getMessage());

			try {
				response.getWriter().write(objectMapper.writeValueAsString(rsp));
				response.getWriter().flush();
				response.getWriter().close();
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	
	@ApiOperation(value = "clientId获取token")
	@PostMapping("/oauth/client/token")
	@LogAnnotation(module = "auth-server", recordRequestParam = false)
	public void getClientTokenInfo(HttpServletRequest request, HttpServletResponse response) {

		String clientId = request.getHeader("client_id");
		String clientSecret = request.getHeader("client_secret");
		try {

			if (clientId == null || "".equals(clientId)) {
				throw new UnapprovedClientAuthenticationException("请求参数中无clientId信息");
			}

			if (clientSecret == null || "".equals(clientSecret)) {
				throw new UnapprovedClientAuthenticationException("请求参数中无clientSecret信息");
			}

			RedisClientDetailsService clientDetailsService = SpringUtil.getBean(RedisClientDetailsService.class);

			ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

			if (clientDetails == null) {
				throw new UnapprovedClientAuthenticationException("clientId对应的信息不存在");
			} else if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
				throw new UnapprovedClientAuthenticationException("clientSecret不匹配");
			}

			Map<String, String> map = new HashMap<>();
			map.put("client_secret", clientSecret);
			map.put("client_id", clientId);
			map.put("grant_type", "client_credentials");
			TokenRequest tokenRequest = new TokenRequest(map, clientId, clientDetails.getScope(), "client_credentials");

			OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

			AuthorizationServerTokenServices authorizationServerTokenServices = SpringUtil
					.getBean("defaultAuthorizationServerTokenServices", AuthorizationServerTokenServices.class);
			OAuth2RequestFactory requestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);
			ClientCredentialsTokenGranter clientCredentialsTokenGranter = new ClientCredentialsTokenGranter(
					authorizationServerTokenServices, clientDetailsService, requestFactory);

			clientCredentialsTokenGranter.setAllowRefresh(true);
			OAuth2AccessToken oAuth2AccessToken = clientCredentialsTokenGranter.grant("client_credentials",
					tokenRequest);

			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(oAuth2AccessToken));
			response.getWriter().flush();
			response.getWriter().close();

		} catch (Exception e) {

			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType("application/json;charset=UTF-8");
			Map<String, String> rsp = new HashMap<>();
			rsp.put("resp_code", HttpStatus.UNAUTHORIZED.value() + "");
			rsp.put("rsp_msg", e.getMessage());

			try {
				response.getWriter().write(objectMapper.writeValueAsString(rsp));
				response.getWriter().flush();
				response.getWriter().close();
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	@ApiOperation(value = "access_token刷新token")
	@PostMapping(value = "/oauth/refresh/token", params = "access_token")
	public void refreshTokenInfo(String access_token, HttpServletRequest request, HttpServletResponse response) {

		// 拿到当前用户信息
		try {
			Authentication user = SecurityContextHolder.getContext().getAuthentication();

			if (user != null) {
				if (user instanceof OAuth2Authentication) {
					Authentication athentication = (Authentication) user;
					OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) athentication.getDetails();
				}

			}
			OAuth2AccessToken accessToken = tokenStore.readAccessToken(access_token);
			OAuth2Authentication auth = (OAuth2Authentication) user;
			RedisClientDetailsService clientDetailsService = SpringUtil.getBean(RedisClientDetailsService.class);

			ClientDetails clientDetails = clientDetailsService
					.loadClientByClientId(auth.getOAuth2Request().getClientId());

			AuthorizationServerTokenServices authorizationServerTokenServices = SpringUtil
					.getBean("defaultAuthorizationServerTokenServices", AuthorizationServerTokenServices.class);
			OAuth2RequestFactory requestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);

			RefreshTokenGranter refreshTokenGranter = new RefreshTokenGranter(authorizationServerTokenServices,
					clientDetailsService, requestFactory);

			Map<String, String> map = new HashMap<>();
			map.put("grant_type", "refresh_token");
			map.put("refresh_token", accessToken.getRefreshToken().getValue());
			TokenRequest tokenRequest = new TokenRequest(map, auth.getOAuth2Request().getClientId(),
					auth.getOAuth2Request().getScope(), "refresh_token");

			OAuth2AccessToken oAuth2AccessToken = refreshTokenGranter.grant("refresh_token", tokenRequest);

			tokenStore.removeAccessToken(accessToken);

			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(oAuth2AccessToken));
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType("application/json;charset=UTF-8");
			Map<String, String> rsp = new HashMap<>();
			rsp.put("resp_code", HttpStatus.UNAUTHORIZED.value() + "");
			rsp.put("rsp_msg", e.getMessage());

			try {
				response.getWriter().write(objectMapper.writeValueAsString(rsp));
				response.getWriter().flush();
				response.getWriter().close();
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	/**
	 * 移除access_token和refresh_token
	 * 
	 * @param access_token
	 */
	@ApiOperation(value = "移除token")
	@PostMapping(value = "/oauth/remove/token", params = "access_token")
	public void removeToken(String access_token) {

		// 拿到当前用户信息
		Authentication user = SecurityContextHolder.getContext().getAuthentication();

		if (user != null) {
			if (user instanceof OAuth2Authentication) {
				Authentication athentication = (Authentication) user;
				OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) athentication.getDetails();
			}

		}
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(access_token);
		if (accessToken != null) {
			// 移除access_token
			tokenStore.removeAccessToken(accessToken);

			// 移除refresh_token
			if (accessToken.getRefreshToken() != null) {
				tokenStore.removeRefreshToken(accessToken.getRefreshToken());
			}

		}
	}

	@ApiOperation(value = "获取token信息")
	@PostMapping(value = "/oauth/get/token", params = "access_token")
	public OAuth2AccessToken getTokenInfo(String access_token) {

		// 拿到当前用户信息
		Authentication user = SecurityContextHolder.getContext().getAuthentication();

		if (user != null) {
			if (user instanceof OAuth2Authentication) {
				Authentication athentication = (Authentication) user;
				OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) athentication.getDetails();
			}

		}
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(access_token);

		return accessToken;

	}

	/**
	 * 当前登陆用户信息
	 * security获取当前登录用户的方法是SecurityContextHolder.getContext().getAuthentication()
	 * 这里的实现类是org.springframework.security.oauth2.provider.OAuth2Authentication
	 * 
	 * @return
	 */
	@ApiOperation(value = "当前登陆用户信息")
	@RequestMapping(value = { "/oauth/userinfo" }, produces = "application/json") // 获取用户信息。/auth/user
	public Map<String, Object> getCurrentUserDetail() {
		Map<String, Object> userInfo = new HashMap<>();
		userInfo.put("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		logger.debug("认证详细信息:" + SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

		List<SysPermission> permissions = new ArrayList<>();

		new ArrayList(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).forEach(o -> {
			SysPermission sysPermission = new SysPermission();
			sysPermission.setPermission(o.toString());
			permissions.add(sysPermission);
		});
		// userInfo.put("authorities",
		// AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities())
		// );
		userInfo.put("permissions", permissions);

		userInfo.put("resp_code", "200");

		logger.info("返回信息:{}", userInfo);

		return userInfo;
	}

	@ApiOperation(value = "token列表")
	@PostMapping("/oauth/token/list")
	public PageResult<HashMap<String, String>> getUserTokenInfo(@RequestParam Map<String, Object> params)
			throws Exception {
		List<HashMap<String, String>> list = new ArrayList<>();

		Set<String> keys = redisTemplate.keys("access:" + "*") ;
//        Object key1 = keys.toArray()[0];
//        Object token1 = redisTemplate.opsForValue().get(key1);
		//根据分页参数获取对应数据
	//	List<String> pages = findKeysForPage("access:" + "*", MapUtils.getInteger(params, "page"),MapUtils.getInteger(params, "limit"));

		for (Object key: keys.toArray()) {
//			String key = page;
//			String accessToken = StringUtils.substringAfter(key, "access:");
//			OAuth2AccessToken token = tokenStore.readAccessToken(accessToken);
            OAuth2AccessToken token = (OAuth2AccessToken)redisTemplate.opsForValue().get(key);
			HashMap<String, String> map = new HashMap<String, String>();

			try {
				map.put("token_type", token.getTokenType());
				map.put("token_value", token.getValue());
				map.put("expires_in", token.getExpiresIn()+"");
			} catch (Exception e) {
				 
			}
			
			
			OAuth2Authentication oAuth2Auth = tokenStore.readAuthentication(token);
			Authentication authentication = oAuth2Auth.getUserAuthentication();

			map.put("client_id", oAuth2Auth.getOAuth2Request().getClientId());
			map.put("grant_type", oAuth2Auth.getOAuth2Request().getGrantType());
			
			if (authentication instanceof UsernamePasswordAuthenticationToken) {
				UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
			
				if(authenticationToken.getPrincipal() instanceof LoginAppUser ){
					LoginAppUser user = (LoginAppUser) authenticationToken.getPrincipal();
					map.put("user_id", user.getId()+"");
					map.put("user_name", user.getUsername()+"");
					map.put("user_head_imgurl", user.getHeadImgUrl()+"");
				}
				
				
			}else if (authentication instanceof PreAuthenticatedAuthenticationToken ){
				//刷新token方式
				PreAuthenticatedAuthenticationToken authenticationToken = (PreAuthenticatedAuthenticationToken) authentication;
				if(authenticationToken.getPrincipal() instanceof LoginAppUser ){
					LoginAppUser user = (LoginAppUser) authenticationToken.getPrincipal();
					map.put("user_id", user.getId()+"");
					map.put("user_name", user.getUsername()+"");
					map.put("user_head_imgurl", user.getHeadImgUrl()+"");
				}

			}
			list.add(map);

		}



		return PageResult.<HashMap<String, String>>builder().data(list).code(0).count((long) keys.size()).build();

	}

	public List<String> findKeysForPage(String patternKey, int pageNum, int pageSize) {

		Set<String> execute = redisTemplate.execute(new RedisCallback<Set<String>>() {

			@Override
			public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {

				Set<String> binaryKeys = new HashSet<>();

				Cursor<byte[]> cursor = connection
						.scan(new ScanOptions.ScanOptionsBuilder().match(patternKey).count(1000).build());
				int tmpIndex = 0;
				int startIndex = (pageNum - 1) * pageSize;
				int end = pageNum * pageSize;
				while (cursor.hasNext()) {
					if (tmpIndex >= startIndex && tmpIndex < end) {
						binaryKeys.add(new String(cursor.next()));
						tmpIndex++;
						continue;
					}

					// 获取到满足条件的数据后,就可以退出了
					if (tmpIndex >= end) {
						break;
					}

					tmpIndex++;
					cursor.next();
				}
				connection.close();
				return binaryKeys;
			}
		});

		List<String> result = new ArrayList<String>(pageSize);
		result.addAll(execute);
		return result;
	}

}
