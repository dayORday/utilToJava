package com.example.demo.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * 封装字符串操作的基础方法
 * 
 * @version 1.0 
 * @since JDK1.8 
 * @author zhangjingtao 
 * @company 洛阳艾克科技有限公司 
 * @copyright (c) 2018 LuoYang ARC Co'Ltd Inc. All rights reserved. 
 * @date 2018年1月17日 下午3:35:32
 */

public class StringUtils {
	/**
	 * 
	 * 连接字符串
	 * 
	 * @param list
	 * @param delimited
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月17日 下午3:37:14
	 */
	public static String listToDelimitedString(List list, String delimited){
		if(list == null || list.size()<1){
			return "";
		}
		StringBuilder stringBuilder = new StringBuilder();
		for(Object object : list){
			stringBuilder.append(object.toString()+delimited);
		}
		stringBuilder.deleteCharAt(stringBuilder.length()-1);
		return stringBuilder.toString();
	}
	
	/**
	 * 
	 * url 参数拼接
	 * 
	 * @param map 参数集合
	 * @param keyLower 是否将key转换为小写
	 * @param valueUrlencode 是否进行encode
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月17日 下午3:39:35
	 */
	public static String mapJoin(Map<String, String> map,boolean keyLower,boolean valueUrlencode){
		StringBuilder stringBuilder = new StringBuilder();
		for(String key :map.keySet()){
			if(map.get(key)!=null&&!"".equals(map.get(key))){
				try {
					String temp = (key.endsWith("_")&&key.length()>1)?key.substring(0,key.length()-1):key;
					stringBuilder.append(keyLower?temp.toLowerCase():temp)
								 .append("=")
								 .append(valueUrlencode?URLEncoder.encode(map.get(key),"utf-8").replace("+", "%20"):map.get(key))
								 .append("&");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		if(stringBuilder.length()>0){
			stringBuilder.deleteCharAt(stringBuilder.length()-1);
		}
		return stringBuilder.toString();
	}
	/**
	 * 
	 * 测试
	 * 
	 * @param args 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月17日 下午3:37:37
	 */
	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		for(int i=0;i<10;i++){
			list.add("*"+i);
		}
		System.out.println(list);
		System.out.println(listToDelimitedString(list,","));
	}
	
	/**more method 
	 * 请移步StringUtil
	 *  StringUtil::startsWith(string $subject, string $prefix) : boolean
		StringUtil::endsWith(string $subject, string $suffix) : boolean
		StringUtil::contains(string $subject, string $needle) : boolean
		StringUtil::containsAny(string $subject, string[] $needles) : boolean
		StringUtil::containsAll(string $subject, string[] $needles) : boolean
		StringUtil::removePrefix(string $subject, string $prefix) : string
		StringUtil::removeSuffix(string $subject, string $suffix) : string
		StringUtil::replace(string $subject, string $search, string $replace) : string
		StringUtil::replace(string $subject, array<string, string> $searchReplaceMapping) : string
	 * 
	 */
	
}

