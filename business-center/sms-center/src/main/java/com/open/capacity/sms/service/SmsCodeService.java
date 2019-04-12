package com.open.capacity.sms.service;

import com.open.capacity.sms.model.SmsCode;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2017年10月12日 上午22:57:51
 * 短信验证码service
 */
public interface SmsCodeService {

	SmsCode generateCode(String phone);

	String matcheCodeAndGetPhone(String key, String code, Boolean delete, Integer second);
}
