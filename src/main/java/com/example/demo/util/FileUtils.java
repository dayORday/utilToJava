package com.example.demo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 
 * 文件的处理
 * 
 * @version 1.0 
 * @since JDK1.7 
 * @date 2018年1月18日 上午9:06:10
 */

public class FileUtils {
	
	public static final String FOLDER_SEPARATOR  = "/";
	public static final char EXTENSION_SEPARATOR = '.'; 
	
	/**
	 * 
	 * 文件是否存在
	 * 
	 * @param filePath 文件路径
	 * @param isNew  是否新创建文件 true / false
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 上午9:10:02
	 */
	public static boolean isExist(String filePath,boolean isNew) {
		File file = new File(filePath);
		if(!file.exists() && isNew){
			return file.mkdirs();
		}
		return false;
	}
	/**
	 * 
	 * 获取文件名称 prefix + date + 随机数 + suffix + type 
	 * 
	 * @param type
	 * @param prefix
	 * @param suffix
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 上午9:18:32
	 */
	public static String getFileName(String type,String prefix,String suffix) {
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyyhhmmss");
		String t = date.format(formatter);
		String random = RandomUtils.generateMixString(10);
		return prefix+t+random+suffix+"."+type;
	}
	
	/**
	 * 
	 * 获取文件大小 
	 * 
	 * @param file
	 * @return
	 * @throws IOException 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 上午9:36:43
	 */
	public static long getFileSize(File file) throws IOException {
		long size = 0;
		if (file.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			size = fis.available();
		} else {
			file.createNewFile();
		}
		return size;
	}
	
	/**
	 * 
	 * 获取文件名后缀
	 * 
	 * @param file
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 上午9:41:43
	 */
	public static String getFileSuffix(String file) {
		if (file == null) {
			return null;
		}
		int extIndex = file.lastIndexOf(EXTENSION_SEPARATOR);
		if (extIndex == -1) {
			return null;
		}
		int folderIndex = file.lastIndexOf(FOLDER_SEPARATOR);
		if (folderIndex > extIndex) {
			return null;
		}
		return file.substring(extIndex + 1);
	}
	/**
	 * 
	 *  获取文件的md5
	 * 
	 * @param file
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 上午9:43:19
	 */
	public static String getFileMD5(File file){
		if (!file.exists() || !file.isFile()) {  
            return null;  
        }  
        MessageDigest digest = null;  
        FileInputStream in = null;  
        byte buffer[] = new byte[1024];  
        int len;  
        try {  
            digest = MessageDigest.getInstance("MD5");  
            in = new FileInputStream(file);  
            while ((len = in.read(buffer, 0, 1024)) != -1) {  
                digest.update(buffer, 0, len);  
            }  
            in.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
        BigInteger bigInt = new BigInteger(1, digest.digest());  
        return bigInt.toString(16);  
	}
	
	public static void main(String[] args) throws IOException {
//		System.out.println(isExist("E:\\img1",true));
//		System.out.println(getFileName("txt","IP","PORT"));
		File file = new File("E:\\img\\timg2.jpg");
		System.out.println(getFileMD5(file));
//		System.out.println(getFileSize(file));
		//868a428a64bb4a724f0de84b17dff813
	}
}

