
package com.open.capacity.utils.encrypt;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import com.alibaba.fastjson.JSON;

/**
 * 解密数据
 *
 */
@RestControllerAdvice
public class ParamEncryptRequestBodyAdvice implements RequestBodyAdvice {
	private static final Logger log = LoggerFactory.getLogger(ParamEncryptRequestBodyAdvice.class);

	@Override
	public boolean supports(MethodParameter methodParameter, Type type,
			Class<? extends HttpMessageConverter<?>> aClass) {
		return methodParameter.hasParameterAnnotation(RequestBody.class);
	}

	@Override
	public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter,
			Type type, Class<? extends HttpMessageConverter<?>> aClass) {
		return o;
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter,
			Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
		try {
			boolean encode = false;
			if (methodParameter.getMethod().isAnnotationPresent(SecurityParameter.class)) {
				SecurityParameter serializedField = (SecurityParameter) methodParameter
						.getMethodAnnotation(SecurityParameter.class);
				encode = serializedField.inDecode();
			}

			if (encode) {
				log.info("对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行解密");
				 return new ParamEncryptRequestBodyAdvice.DefaultHttpInputMessage(httpInputMessage);
			} else {
				return httpInputMessage;
			}
		} catch (Exception e) {
			log.error(
					"对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行解密出现异常：" + e.getMessage());
		}
		 return httpInputMessage;
	}

	@Override
	public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type,
			Class<? extends HttpMessageConverter<?>> aClass) {
		return o;
	}
	class DefaultHttpInputMessage implements HttpInputMessage {
        private HttpHeaders headers;
        private InputStream body;

        public DefaultHttpInputMessage(HttpInputMessage inputMessage) throws Exception {
            this.headers = inputMessage.getHeaders();
            this.body = IOUtils.toInputStream(DESHelper.decrypt(this.easpString(IOUtils.toString(inputMessage.getBody(), "UTF-8"))), "UTF-8");
        }

        public InputStream getBody() throws IOException {
            return this.body;
        }

        public HttpHeaders getHeaders() {
            return this.headers;
        }

        public String easpString(String requestData) {
            if (requestData != null && !requestData.equals("")) {
                if (!requestData.contains("\"access_token\":")) {
                	
                	throw new RuntimeException("参数【access_token】缺失异常！");
                }else{
                	
                	Map maps = (Map)JSON.parse(requestData);  
                	
                	if("" ==maps.get("access_token") ){
                		throw new RuntimeException("参数【access_token】缺失异常！");
                	} else{
                		return   (String) maps.get("access_token" );
                	}
               }
                	
            } 
            return "" ;
        }
    }
}