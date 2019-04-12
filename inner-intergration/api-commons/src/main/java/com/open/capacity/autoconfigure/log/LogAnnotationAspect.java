package com.open.capacity.autoconfigure.log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.open.capacity.annotation.log.LogAnnotation;
import com.open.capacity.log.service.LogService;
import com.open.capacity.log.service.impl.LogServiceImpl;
import com.open.capacity.model.log.SysLog;
import com.open.capacity.model.system.LoginAppUser;
import com.open.capacity.utils.SpringUtils;
import com.open.capacity.utils.SysUserUtil;

/**
 * 保存日志
 * 
 * @author owen
 * @create 2017年7月2日
 */
@Aspect
@Order(-1) // 保证该AOP在@Transactional之前执行
public class LogAnnotationAspect {

	private static final Logger logger = LoggerFactory.getLogger(LogAnnotationAspect.class);

	@Around("@annotation(ds)")
	public Object logSave(ProceedingJoinPoint joinPoint, LogAnnotation ds) throws Throwable {

		// 请求流水号
		String transid = getRandom();
		// 记录开始时间
		long start = System.currentTimeMillis();
		// 获取方法参数
		String url = null;
		String httpMethod = null;
		Object result = null;
		List<Object> httpReqArgs = new ArrayList<Object>();
		SysLog log = new SysLog();
		log.setCreateTime(new Date());
		LoginAppUser loginAppUser = SysUserUtil.getLoginAppUser();
		if (loginAppUser != null) {
			log.setUsername(loginAppUser.getUsername());
		}

		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

		LogAnnotation logAnnotation = methodSignature.getMethod().getDeclaredAnnotation(LogAnnotation.class);
		log.setModule(logAnnotation.module() + ":" + methodSignature.getDeclaringTypeName() + "/"
				+ methodSignature.getName());

		Object[] args = joinPoint.getArgs();// 参数值
		url =  methodSignature.getDeclaringTypeName() + "/"+ methodSignature.getName();
		for (Object object : args) {
			if (object instanceof HttpServletRequest) {
				HttpServletRequest request = (HttpServletRequest) object;
				url = request.getRequestURI();
				httpMethod = request.getMethod();
			} else if (object instanceof HttpServletResponse) {
			} else {
				
				httpReqArgs.add(object);
			}
		}

		try {
			String params = JSONObject.toJSONString(httpReqArgs);
			log.setParams(params);
			// 打印请求参数参数
			logger.info("开始请求，transid={},  url={} , httpMethod={}, reqData={} ", transid, url, httpMethod, params);
		} catch (Exception e) {
			logger.error("记录参数失败：{}", e.getMessage());
		}

		try {
			// 调用原来的方法
			result = joinPoint.proceed();
			log.setFlag(Boolean.TRUE);
		} catch (Exception e) {
			log.setFlag(Boolean.FALSE);
			log.setRemark(e.getMessage());

			throw e;
		} finally {

			CompletableFuture.runAsync(() -> {
				try {
					if (logAnnotation.recordRequestParam()) {
						LogService logService = SpringUtils.getBean(LogServiceImpl.class);
						logService.save(log);
					}
				} catch (Exception e) {
					logger.error("记录参数失败：{}", e.getMessage());
				}

			});
			// 获取回执报文及耗时
			logger.info("请求完成, transid={}, 耗时={}, resp={}:", transid, (System.currentTimeMillis() - start),
					result == null ? null : JSON.toJSONString(result));

		}
		return result;
	}

	/**
	 * 生成日志随机数
	 * 
	 * @return
	 */
	public String getRandom() {
		int i = 0;
		StringBuilder st = new StringBuilder();
		while (i < 5) {
			i++;
			st.append(ThreadLocalRandom.current().nextInt(10));
		}
		return st.toString() + System.currentTimeMillis();
	}

}