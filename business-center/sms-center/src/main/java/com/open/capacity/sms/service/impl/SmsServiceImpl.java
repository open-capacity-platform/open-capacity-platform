package com.open.capacity.sms.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.open.capacity.commons.PageResult;
import com.open.capacity.sms.dao.SmsDao;
import com.open.capacity.sms.model.Sms;
import com.open.capacity.sms.service.SmsService;

import lombok.extern.slf4j.Slf4j;


/**
 * * 程序名 : AliyunSmsConfig
 * 建立日期: 2018-07-09
 * 作者 : someday
 * 模块 : 短信中心
 * 描述 : 调用阿里短信接口
 * 备注 : version20180709001
 * <p>
 * 修改历史
 * 序号 	       日期 		        修改人 		         修改原因
 */
@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

	@Autowired
	private IAcsClient acsClient;
	@Value("${aliyun.sms.sign.name:xxxxxx}")
	private String signName;
	@Value("${aliyun.sms.template.code:xxxxx}")
	private String templateCode;

	@Autowired
	private SmsDao smsDao;

	@Override
	public SendSmsResponse sendSmsMsg(Sms sms) {
		if (sms.getSignName() == null) {
			sms.setSignName(this.signName);
		}

		if (sms.getTemplateCode() == null) {
			sms.setTemplateCode(this.templateCode);
		}

		// 阿里云短信官网demo代码
		SendSmsRequest request = new SendSmsRequest();
		request.setMethod(MethodType.POST);
		request.setPhoneNumbers(sms.getPhone());
		request.setSignName(sms.getSignName());
		request.setTemplateCode(sms.getTemplateCode());
		request.setTemplateParam(sms.getParams());
		request.setOutId(sms.getId().toString());

		SendSmsResponse response = null;
//		测试时不需要开此 add by someday begin
//		try {
//			response = acsClient.getAcsResponse(request);
//			if (response != null) {
//				log.info("发送短信结果：code:{}，message:{}，requestId:{}，bizId:{}", response.getCode(), response.getMessage(),
//						response.getRequestId(), response.getBizId());
//
//				sms.setCode(response.getCode());
//				sms.setMessage(response.getMessage());
//				sms.setBizId(response.getBizId());
//			}
//		} catch (ClientException e) {
//			e.printStackTrace();
//		}
//		测试时不需要开此 add by someday end
		update(sms);

		return response;
	}

	@Transactional
	@Override
	public void save(Sms sms, Map<String, String> params) {
		if (!CollectionUtils.isEmpty(params)) {
			sms.setParams(JSONObject.toJSONString(params));
		}

		sms.setCreateTime(new Date());
		sms.setUpdateTime(sms.getCreateTime());
		sms.setDay(sms.getCreateTime());

		smsDao.save(sms);
	}

	@Transactional
	@Override
	public void update(Sms sms) {
		sms.setUpdateTime(new Date());
		smsDao.update(sms);
	}

	@Override
	public Sms findById(Long id) {
		return smsDao.findById(id);
	}

	@Override
	public PageResult<Sms> findSms(Map<String, Object> params) {
		//设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
        PageHelper.startPage(MapUtils.getInteger(params, "page"),MapUtils.getInteger(params, "limit"),true);

        List<Sms> list = smsDao.findList(params);
        PageInfo<Sms> pageInfo = new PageInfo<>(list);
		return PageResult.<Sms>builder().data(pageInfo.getList()).code(0).count(pageInfo.getTotal()).build();
	}

}
