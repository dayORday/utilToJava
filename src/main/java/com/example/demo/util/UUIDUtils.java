package com.example.demo.util;

import java.util.UUID;

/**
 * 
 * 生成uuid 
 * 
 * @version 1.0 
 * @since JDK1.7 
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

