package com.example.demo.util;

import java.util.UUID;

/**
 * 
 * 生成uuid 
 * 
 * @version 1.0 
 * @since JDK1.7 
 * @author zhangjingtao 
 * @company 洛阳艾克科技有限公司 
 * @copyright (c) 2018 LuoYang ARC Co'Ltd Inc. All rights reserved. 
 * @date 2018年1月24日 下午3:16:57
 */
public class UUIDUtils {
	
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		return uuid;
	}
	
	public static void main(String[] args) {
		System.out.println(getUUID());
	}
}

