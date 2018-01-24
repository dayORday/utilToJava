package com.example.demo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 正则工具类
 * 
 * @version 1.0 
 * @since JDK1.8 
 * @author zhangjingtao 
 * @company 洛阳艾克科技有限公司 
 * @copyright (c) 2018 LuoYang ARC Co'Ltd Inc. All rights reserved. 
 * @date 2018年1月24日 下午1:43:17
 */

public class RegexUtils {
	/**
	 * 
	 * 判断输入的字符串是否满足正则表达式
	 * 
	 * @param str
	 * @param regex
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月24日 下午1:47:33
	 */
	public  static boolean findRegex (String str,String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		boolean flag = matcher.find();
		return flag;
	}
	/**
	 * 
	 * 输入的字符串是否符合邮箱格式
	 * 
	 * @return 
	 * @author zhangjingtao 
	 * @date 2018年1月24日 下午1:48:31
	 */
	public static boolean isEmail(String email) {
		if (null == email || email.length() < 1 || email.length() > 256) {
			return false;
		}
		/**
		 *  matches:整个匹配，只有整个字符序列完全匹配成功，才返回True，否则返回False。但如果前部分匹配成功，将移动下次匹配的位置。
		 *	lookingAt:部分匹配，总是从第一个字符进行匹配,匹配成功了不再继续匹配，匹配失败了,也不继续匹配。
		 *	find:部分匹配，从当前位置开始匹配，找到一个匹配的子串，将移动下次匹配的位置。
		 *	reset:给当前的Matcher对象配上个新的目标，目标是就该方法的参数；如果不给参数，reset会把Matcher设到当前字符串的开始处。
		 */
		Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
		boolean flag = pattern.matcher(email).matches();
		return flag;
	}
	
	/**
	 * 
	 * 是否全是中文字符
	 * @return 
	 * @author zhangjingtao 
	 * @date 2018年1月24日 下午2:00:48
	 */
	public static boolean isChinese(String str) {
		Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
		boolean flag = pattern.matcher(str).matches();
		return flag;
	}
	/**
	 * 
	 * 判断是否为浮点型
	 * 
	 * @param value
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月24日 下午2:03:03
	 */
	public static boolean isDouble(String value) {
		Pattern pattern = Pattern.compile("^[-\\+]?\\d+\\.\\d+$");
		boolean flag = pattern.matcher(value).matches();
		return flag;
	}
	/**
	 * 
	 *  判断是否为整数
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月24日 下午2:04:08
	 */
	public static boolean isInt(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+$");
		boolean flag = pattern.matcher(str).matches();
		return flag;
	}
}

