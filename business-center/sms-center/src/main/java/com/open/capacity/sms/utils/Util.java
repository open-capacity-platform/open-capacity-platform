package com.open.capacity.sms.utils;

import java.util.Random;

/**
 * * 程序名 : Util
 * 建立日期: 2018-07-09
 * 作者 : someday
 * 模块 : 短信中心
 * 描述 : 短信验证码生成器
 * 备注 : version20180709001
 * <p>
 * 修改历史
 * 序号 	       日期 		        修改人 		         修改原因
 */
public class Util {

	private static String[] NUMBERS = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
	private static Random RANDOM = new Random();

	/**
	 * 生成length位随机数值字符串
	 * 
	 * @param length
	 * @return
	 */
	public static String randomCode(int length) {
		StringBuilder builder = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			builder.append(NUMBERS[RANDOM.nextInt(NUMBERS.length)]);
		}

		return builder.toString();
	}
}
