package com.open.capacity.utils.encrypt;

import java.lang.annotation.Annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestControllerAdvice
public class ParamEncryptResponseBodyAdvice implements ResponseBodyAdvice {
	private static final Logger log = LoggerFactory.getLogger(ParamEncryptResponseBodyAdvice.class);
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public boolean supports(MethodParameter returnType, Class converterType) {
		return returnType.hasMethodAnnotation(ResponseBody.class);
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType selectedContentType,
			Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		if (null != body) {

			boolean encode = false;
			if (methodParameter.getMethod()
					.isAnnotationPresent((Class<? extends Annotation>) SecurityParameter.class)) {
				final SecurityParameter serializedField = (SecurityParameter) methodParameter
						.getMethodAnnotation((Class) SecurityParameter.class);
				encode = serializedField.outEncode();
			}
			if (encode) {
				log.info("对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行加密");
				final ObjectMapper objectMapper = new ObjectMapper();
				try {
					final String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
					return DESHelper.encrypt(result);
				} catch (Exception e) {
					log.info(
							"对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行加密出现异常:" + e.getMessage());
				}
			}
			
		}

		return body;
	}
}