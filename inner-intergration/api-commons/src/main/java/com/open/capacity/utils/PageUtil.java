package com.open.capacity.utils;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

 
/**
* @author 作者 owen E-mail: 624191343@qq.com
* @version 创建时间：2017年11月12日 上午22:57:51
* 分页参数处理工具
* url 地址重写 ?start=1&length=10
*/
@Slf4j
public class PageUtil {

	/**
	 * 分页参数，起始位置，从0开始
	 */
	public static final String PAGE = "page";
	/**
	 * 分页参数，每页数据条数
	 */
	public static final String LIMIT = "limit";

	/**
	 * 转换并校验分页参数<br>
	 * mybatis中limit #{start, JdbcType=INTEGER}, #{length,
	 * JdbcType=INTEGER}里的类型转换貌似失效<br>
	 * 我们这里先把他转成Integer的类型
	 * 
	 * @param params
	 * @param required
	 * 分页参数是否是必填
	 */
	public static void pageParamConver(Map<String, Object> params, boolean required) {
		if (required) {// 分页参数必填时，校验参数
			if (params == null || !params.containsKey(PAGE) || !params.containsKey(LIMIT)) {
				throw new IllegalArgumentException("请检查分页参数," + PAGE + "," + LIMIT);
			}
		}

		if (!CollectionUtils.isEmpty(params)) {
			if (params.containsKey(PAGE)) {
				Integer start = MapUtils.getInteger(params, PAGE);
				Integer length = MapUtils.getInteger(params, LIMIT);
				if (start < 0) {
					log.error("page：{}，重置为0", start);
					start = 0;
				}
				params.put(PAGE, (start-1)*length);
			}

			if (params.containsKey(LIMIT)) {
				Integer length = MapUtils.getInteger(params, LIMIT);
				if (length < 0) {
					log.error("limit：{}，重置为0", length);
					length = 0;
				}
				params.put(LIMIT, length);
			}
		}
	}
}
