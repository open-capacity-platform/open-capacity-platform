package com.open.capacity.utils;

import java.util.Date;

/**
 * Title: NullProcessUtil
 * Description: 处理NULL对象的工具类
 * Date: 2004-10-14
 * @version 1.0
 */
public class NullProcessUtil {
	 	
	/**
	 * 对数据对象进行null检测，如果为null，则返回相应的字符串
	 * @param obj 需要检测的数据对象
	 * @param target 如果obj为null，需要返回的目的字符串
	 * @return 经过空值处理后的字符串
	 */
	public static String nvlToString(Object obj,String target){
		return obj==null?target:String.valueOf(obj);
	}
	
	/**
	 * 对Integer数据对象进行null检测，如果为null，则返回相应int值
	 * @param obj 需要检测的数据对象
	 * @param target 如果obj为null，需要返回的int型数值
	 * @return 经过空值处理后的int型数值
	 */
	public static int nvlToInt(Integer obj,int target){
		return obj==null?target:obj.intValue();
	}
	
	/**
	 * 对Long数据对象进行null检测，如果为null，则返回相应long值
	 * @param obj 需要检测的数据对象
	 * @param target 如果obj为null，需要返回的long型数值
	 * @return 经过空值处理后的long型数值
	 */
	public static long nvlToLong(Long obj,long target){
		return obj==null?target:obj.longValue();
	}
	
	/**
	 * 对Float数据对象进行null检测，如果为null，则返回相应float值
	 * @param obj 需要检测的数据对象
	 * @param target 如果obj为null，需要返回的float型数值
	 * @return 经过空值处理后的float型数值
	 */
	public static float nvlToFloat(Float obj,float target){
		return obj==null?target:obj.floatValue();
	}
	
	/**
	 * 对Double数据对象进行null检测，如果为null，则返回相应double值
	 * @param obj 需要检测的数据对象
	 * @param target 如果obj为null，需要返回的double型数值
	 * @return 经过空值处理后的float型数值
	 */
	public static double nvlToDouble(Double obj,double target){
		return obj==null?target:obj.doubleValue();
	}
	
	/**
	 * 对Date数据对象进行null检测，如果为null，则返回相应Date值
	 * @param obj 需要检测的数据对象
	 * @param target 如果obj为null，需要返回的Date
	 * @return 经过空值处理后的Date型数值
	 */
	public static Date nvlToDate(Date obj,Date target){
		return obj==null?target:obj;
	}
}
