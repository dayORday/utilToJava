package com.example.demo.util.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 *  md5加密
 * 
 * @version 1.0 
 * @since JDK1.7 
 * @author zhangjingtao 
 * @company 洛阳艾克科技有限公司 
 * @copyright (c) 2018 LuoYang ARC Co'Ltd Inc. All rights reserved. 
 * @date 2018年1月18日 下午3:45:27
 */

public class MD5Utils {

	protected final static String MD5_KEY = "MD5";
	
	protected final static String SHA_KEY = "SHA1";
	/**
	 * 
	 * md5 加密  
	 * 
	 * @param value 需要加密的字符串 
	 * @param key  加密方式 md5 或者 sha1
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 下午4:14:44
	 */
	protected static String encrypt(String value,String key) {
		try {
			// 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
			MessageDigest messageDigest = MessageDigest.getInstance(key);
			// 输入的字符串转换成字节数组
			byte[] inputByteArray = value.getBytes();
			// inputByteArray是输入字符串转换得到的字节数组
			messageDigest.update(inputByteArray);
			// 转换并返回结果，也是字节数组，包含16个元素
			byte[] resultByteArray = messageDigest.digest();
			// 字符数组转换成字符串返回
			return byteArrayToHex(resultByteArray);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	/**
	 * 
	 * 将字节数组转换为字符串
	 * 
	 * @param byteArray
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 下午4:15:26
	 */
	private static String byteArrayToHex(byte[] byteArray) {
		// 首先初始化一个字符数组，用来存放每个16进制字符
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		// new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
		char[] resultCharArray = new char[byteArray.length * 2];
		// 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
		int index = 0;
		for (byte b : byteArray) {
			resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
			resultCharArray[index++] = hexDigits[b & 0xf];
		}
		// 字符数组组合成字符串返回
		return new String(resultCharArray);
	}
	
	public static void main(String[] args) {
		System.out.println(encrypt("扶大厦于将倾",MD5_KEY));
		System.out.println("-----------------------------");
		System.out.println(encrypt("扶大厦于将倾",SHA_KEY));
	}
}

