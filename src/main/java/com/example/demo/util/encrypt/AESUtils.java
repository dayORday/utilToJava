package com.example.demo.util.encrypt;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 
 * AES加解密
 * 
 * @version 1.0
 * @since JDK1.7
 * @date 2018年1月18日 下午4:48:41
 */
public class AESUtils {

	/** 默认秘钥 */
	protected static final String KEY = "NOPO3nzMD3dndwS0MccuMeXCHgVlGOoYyFwLdS24Im2e7YyhB0wrUsyYf0";

	/**
	 * 
	 * 加密
	 * 
	 * @param value
	 * @param key
	 * @return
	 * @throws Exception
	 * 
	 * @date 2018年1月18日 下午4:50:16
	 */
	protected static String encrypt(String value, String key) throws Exception {
		return base64Encode(aesEncryptToBytes(value, key));
	}
	/**
	 * 
	 * 加密 
	 * 
	 * @param bytes
	 * @return 
	 * @date 2018年1月18日 下午4:52:42
	 */
	private static String base64Encode(byte[] bytes) {
		return Base64Utils.encrypt(bytes);
	}

	/**
	 * 
	 * 加密
	 * 
	 * @param content
	 * @param encryptKey
	 * @return
	 * @throws Exception
	 * 
	 * @author zhangjingtao
	 * @date 2018年1月18日 下午4:51:47
	 */
	private static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128, new SecureRandom(encryptKey.getBytes()));
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
		return cipher.doFinal(content.getBytes("utf-8"));
	}
	/**
	 * 
	 * 解密
	 * 
	 * @param encryptValue
	 * @param key
	 * @return
	 * @throws Exception 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 下午5:02:32
	 */
	protected static String decrypt(String encryptValue, String key) throws Exception {
		return aesDecryptByBytes(base64Decode(encryptValue), key);
	}
	/**
	 * 
	 * 解密 
	 * 
	 * @param content
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @throws Exception 
	 * @date 2018年1月18日 下午5:03:42
	 */
	private static  byte[] base64Decode(String content) throws Exception{
		return Base64Utils.decrypt(content);
	}
	/**
	 * 
	 * 解密
	 * 
	 * @param bt
	 * @param key
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @throws NoSuchAlgorithmException 
	 * @throws NoSuchPaddingException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws UnsupportedEncodingException 
	 * @date 2018年1月18日 下午5:05:15
	 */
	private static String aesDecryptByBytes(byte[] encryptBytes,String decryptKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        kgen.init(128, new SecureRandom(decryptKey.getBytes()));  
          
        Cipher cipher = Cipher.getInstance("AES");  
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));  
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
		return new String(decryptBytes,"utf-8");
	}
	
	public static void main(String[] args) {
		String str = "AES start encry !!!";
		try {
			String encodeAfter = encrypt(str,KEY);
			System.out.println("加密后："+encodeAfter);
			String decodeAfter = decrypt(encodeAfter,KEY);
			System.out.println("解密后："+decodeAfter);
		} catch (Exception e) {
		}
	}
}
