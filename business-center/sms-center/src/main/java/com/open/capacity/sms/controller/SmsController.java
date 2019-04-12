package com.open.capacity.sms.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.open.capacity.commons.PageResult;
import com.open.capacity.sms.model.Sms;
import com.open.capacity.sms.model.SmsCode;
import com.open.capacity.sms.service.SmsCodeService;
import com.open.capacity.sms.service.SmsService;
import com.open.capacity.utils.PhoneUtil;

/**
 * * 程序名 : SmsController
 * 建立日期: 2018-07-09
 * 作者 : someday
 * 模块 : 短信中心
 * 描述 : 短信接口
 * 备注 : version20180709001
 * <p>
 * 修改历史
 * 序号 	       日期 		        修改人 		         修改原因
 */
@RestController
public class SmsController {

	@Autowired
	private SmsCodeService smsCodeService;

	@Autowired
	private SmsService smsService;
	
	/**
	 * 发送短信验证码
	 * 
	 * @param phone
	 * @return
	 */
	@PostMapping(value = "/sms-internal/codes", params = { "phone" })
	public SmsCode sendSmsCode(String phone) {
		if (!PhoneUtil.checkPhone(phone)) {
			throw new IllegalArgumentException("手机号格式不正确");
		}

		SmsCode smsCode = smsCodeService.generateCode(phone);

		return smsCode;
	}

	/**
	 * 根据验证码和key获取手机号
	 * 
	 * @param key
	 * @param code
	 * @param delete
	 * 是否删除key
	 * @param second
	 * 不删除时，可重置过期时间（秒）
	 * @return
	 */
	@GetMapping(value = "/sms-internal/phone", params = { "key", "code" })
	public String matcheCodeAndGetPhone(String key, String code, Boolean delete, Integer second) {
		return smsCodeService.matcheCodeAndGetPhone(key, code, delete, second);
	}

	

	/**
	 * 查询短信发送记录
	 * 
	 * @param params
	 * @return
	 */
	@PreAuthorize("hasAuthority('sms:query')")
	@GetMapping("/sms")
	public PageResult<Sms> findSms(@RequestParam Map<String, Object> params) {
		return smsService.findSms(params);
	}
}
