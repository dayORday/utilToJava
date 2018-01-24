package com.example.demo.util.encrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * 
 * DES加解密
 * 
 * @version 1.0 
 * @since JDK1.7 
 * @date 2018年1月18日 下午5:12:28
 */
public class DESUtils {
	/** 默认key */
	protected final static String KEY = "ScAKC0XhadTHT3Al0QIDAQAB";
	/**
	 * 
	 * 加密
	 * 
	 * @param data 待加密的字符串
	 * @param key 秘钥
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 下午5:14:25
	 */
	protected static String encrypt(String data,String key) {  
        String encryptedData = null;  
        try {  
            // DES算法要求有一个可信任的随机数源  
            SecureRandom sr = new SecureRandom();  
            DESKeySpec deskey = new DESKeySpec(key.getBytes());  
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象  
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
            SecretKey secretKey = keyFactory.generateSecret(deskey);  
            // 加密对象  
            Cipher cipher = Cipher.getInstance("DES");  
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);  
            // 加密，并把字节数组编码成字符串  
            encryptedData = new sun.misc.BASE64Encoder().encode(cipher.doFinal(data.getBytes()));  
        } catch (Exception e) {  
            throw new RuntimeException("加密错误，错误信息：", e);  
        }  
        return encryptedData;  
    }
	
	/**
	 * 
	 *  解密
	 * 
	 * @param cryptData
	 * @param key
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 下午5:15:23
	 */
	protected static String decrypt(String cryptData,String key) {  
        String decryptedData = null;  
        try {  
            // DES算法要求有一个可信任的随机数源  
            SecureRandom sr = new SecureRandom();  
            DESKeySpec deskey = new DESKeySpec(key.getBytes());  
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象  
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
            SecretKey secretKey = keyFactory.generateSecret(deskey);  
            // 解密对象  
            Cipher cipher = Cipher.getInstance("DES");  
            cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);  
            // 把字符串解码为字节数组，并解密  
            decryptedData = new String(cipher.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(cryptData)));  
        } catch (Exception e) {  
            throw new RuntimeException("解密错误，错误信息：", e); 
        }  
        return decryptedData;  
    } 
	
	public static void main(String[] args) {
		String  str = "DES jia mi !!";
		String encode = encrypt(str,KEY);
		System.out.println(encode);
		String decode = decrypt(encode,KEY);
		System.out.println(decode);
	}
}

