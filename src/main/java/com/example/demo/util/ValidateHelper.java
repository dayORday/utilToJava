package com.example.demo.util;

import java.util.Collection;
import java.util.Map;

/**
 * 
 * 基础验证 
 * 判断对象 字符 集合是否为空
 * 
 * @version 1.0 
 * @since JDK1.7 
 * @author zhangjingtao 
 * @company 洛阳艾克科技有限公司 
 * @copyright (c) 2018 LuoYang ARC Co'Ltd Inc. All rights reserved. 
 * @date 2018年1月17日 下午4:27:13
 */

public class ValidateHelper {
	/**
	 * 
	 * 判断数据是否为空
	 * 
	 * @param array
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月17日 下午4:30:41
	 */
	public static <T> boolean isArrayEmpty(T[] array) {
		if(array == null || array.length == 0){
			return true;
		}
		return false;
	}
	/**
	 * 
	 * 判断字符串是否为空 
	 * 
	 * @param str
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月17日 下午4:32:18
	 */
	public static boolean isStringEmpty(String str) {
		if(str == null || str.length() == 0){
			return true;
		}
		return false;
	}
	/**
	 * 
	 * 判断集合是否为空
	 * 
	 * @param collection
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月17日 下午4:34:10
	 */
	public static boolean isCollectionEmpty(Collection<?> collection) {
		if (collection == null || collection.isEmpty()) {
			return true;
		}
		return false;
	}
	/**
	 * 
	 * 判断map是否为空
	 * 
	 * @param map
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @param <v>
	 * @date 2018年1月17日 下午4:35:11
	 */
	public static  boolean isMapEmpty(Map map) {
		if(map == null || map.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(isStringEmpty(null));
	}
	
}

