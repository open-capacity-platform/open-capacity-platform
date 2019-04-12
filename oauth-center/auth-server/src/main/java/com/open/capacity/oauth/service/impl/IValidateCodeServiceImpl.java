package com.open.capacity.oauth.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import com.open.capacity.oauth.service.IValidateCodeService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zlt
 * @date 2018/12/10
 */
@Slf4j
@Service
public class IValidateCodeServiceImpl implements IValidateCodeService {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 保存用户验证码，和randomStr绑定
	 * @param deviceId
	 *            客户端生成
	 * @param imageCode
	 *            验证码信息
	 */
	@Override
	public void saveImageCode(String deviceId, String imageCode) {

		String text = imageCode.toLowerCase().toString();

		redisTemplate.execute(new RedisCallback<String>() {

			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {

				// redis info
				connection.set(buildKey(deviceId).getBytes(), imageCode.getBytes());
				connection.expire(buildKey(deviceId).getBytes(), 60*5);
				connection.close();

				return "";
			}
		});

	}

	/**
	 * 获取验证码
	 * @param deviceId
	 *            前端唯一标识/手机号
	 */
	@Override
	public String getCode(String deviceId)  {

		String code = "" ;
		try {
			code = redisTemplate.execute(new RedisCallback<String>() {

				@Override
				public String doInRedis(RedisConnection connection) throws DataAccessException {

					// redis info
					byte[] temp = "".getBytes();
					temp = connection.get(buildKey(deviceId).getBytes()) ;
					connection.close();

					return new String(temp);
				}
			});
		} catch (Exception e) {
			throw new AuthenticationException("验证码不存在"){};
		}
		
		return code ;

	}

	/**
	 * 删除验证码
	 * @param deviceId
	 *            前端唯一标识/手机号
	 */
	@Override
	public void remove(String deviceId) {
		redisTemplate.execute(new RedisCallback<String>() {

			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {

				// redis info
				connection.del(buildKey(deviceId).getBytes());
				connection.close();

				return "";
			}
		});
	}

	/**
	 * 验证验证码
	 */
	@Override
	public void validate(HttpServletRequest request) {
		String deviceId = request.getParameter("deviceId");
		if (StringUtils.isBlank(deviceId)) {
			throw new RuntimeException("请在请求参数中携带deviceId参数");
		}
		String code = this.getCode(deviceId);
		String codeInRequest;
		try {
			codeInRequest = ServletRequestUtils.getStringParameter(request, "validCode");
		} catch (ServletRequestBindingException e) {
			throw new AuthenticationException ("获取验证码的值失败"){};
		}
		if (StringUtils.isBlank(codeInRequest)) {
			throw new AuthenticationException ("请填写验证码"){};
		}

		if (code == null) {
			throw new AuthenticationException ("验证码不存在或已过期"){};
		}

		if (!StringUtils.equals(code, codeInRequest)) {
			throw new AuthenticationException ("验证码不正确"){};
		}

		this.remove(deviceId);
	}

	private String buildKey(String deviceId) {
		return "DEFAULT_CODE_KEY:" + deviceId;
	}
}
