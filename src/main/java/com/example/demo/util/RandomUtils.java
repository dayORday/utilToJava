package com.example.demo.util;

import java.util.Random;

/**
 * 
 * 生成随机字符  
 * 
 * @version 1.0 
 * @since JDK1.7 
 * @date 2018年1月17日 下午3:57:38
 */

public class RandomUtils {

	private static final String ALL_CHAR ="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String LETTER_CHAR="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String NUMBER_CHAR="0123456789";
	/**
	 * 
	 * 生成随机定长的字符数，只包含数字
	 * 
	 * @param length
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月17日 下午4:10:49
	 */
	public static String generateNumber(int length){
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for(int i=0;i<length;i++){
			sb.append(NUMBER_CHAR.charAt(random.nextInt(NUMBER_CHAR.length())));
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * 获取定长的随机数，包含大小写、数字
	 * 
	 * @param length
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月17日 下午4:13:41
	 */
    public static String generateString(int length) { 
        StringBuffer sb = new StringBuffer(); 
        Random random = new Random(); 
        for (int i = 0; i < length; i++) { 
                sb.append(ALL_CHAR.charAt(random.nextInt(ALL_CHAR.length()))); 
        } 
        return sb.toString(); 
    } 
    
    /**
     * 
     * 获取定长的随机数，包含大小写字母
     * 
     * @param length
     * @return 
     * 
     * @author zhangjingtao 
     * @date 2018年1月17日 下午4:14:08
     */
    public static String generateMixString(int length) { 
        StringBuffer sb = new StringBuffer(); 
        Random random = new Random(); 
        for (int i = 0; i < length; i++) { 
                sb.append(LETTER_CHAR.charAt(random.nextInt(LETTER_CHAR.length()))); 
        } 
        return sb.toString(); 
    } 
    
    /**
     * 
     * 获取定长的随机数，只包含小写字母
     * 
     * @param length
     * @return 
     * 
     * @author zhangjingtao 
     * @date 2018年1月17日 下午4:14:42
     */
    public static String generateLowerString(int length) { 
        return generateString(length).toLowerCase(); 
    } 
    
    /**
     * 
     * 获取定长的随机数，只包含大写字母 
     * 
     * @param length
     * @return 
     * 
     * @author zhangjingtao 
     * @date 2018年1月17日 下午4:14:54
     */
    public static String generateUpperString(int length) { 
        return generateString(length).toUpperCase(); 
    } 
    
	public static void main(String[] args) {
		System.out.println(generateUpperString(6));
		
	}
}

