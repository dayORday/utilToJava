package com.example.demo.util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 
 * 文件压缩、解压工具类。文件压缩格式为zip
 * 
 * @version 1.0 
 * @since JDK1.7 
 * @date 2018年1月18日 上午10:19:14
 */
public class ZipUtils {

	/**压缩文件后缀*/
	public static final String ZIP_FILE_SUFFIX  = ".zip";

	/**
	 * 
	 * 方法描述 压缩文件 
	 * 
	 * @param resourcePath
	 * @param targetPath
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 上午10:30:19
	 */
	public static String zipFile(String resourcePath,String targetPath) {
		File resourcesFile = new File(resourcePath); 
		File targetFile = new File(targetPath);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		String targetName = resourcesFile.getName()+ZIP_FILE_SUFFIX;
		ZipOutputStream out = null;
		try {
			FileOutputStream outputStream = new FileOutputStream(targetPath+"//"+targetName);
			out = new ZipOutputStream(new BufferedOutputStream(outputStream));

			compressedFile(out, resourcesFile, "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		}
		return targetPath;
	}
	/**
	 * 
	 * 压缩文件
	 * 注意事项：
	 * 1.打包文件名不能出现汉字否则压缩失败 ，
	 * 2.打包后的第一级目录为空
	 * @param out  压缩文件输出流
	 * @param resourcesFile  源文件
	 * @param dir  压缩目录
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 上午10:36:16
	 */
	public static void compressedFile(ZipOutputStream out,File file,String dir){
		FileInputStream fis = null;
		try {
			if (file.isDirectory()) {	//文件夹
				// 得到文件列表信息
				File[] files = file.listFiles();
				// 将文件夹添加到下一级打包目录
				out.putNextEntry(new ZipEntry(dir+"/"));
				dir = dir.length() == 0 ? "" : dir + "/";
				// 循环将文件夹中的文件打包
				for (int i = 0; i < files.length; i++) {
					compressedFile(out, files[i], dir + files[i].getName()); // 递归处理
				}
			} else { 	//如果是文件则打包处理
				fis = new FileInputStream(file);

				out.putNextEntry(new ZipEntry(dir));
				// 进行写操作
				int j = 0;
				byte[] buffer = new byte[1024];
				while ((j = fis.read(buffer)) > 0) {
					out.write(buffer, 0, j);
				}
				out.flush();
				// 关闭输入流
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void decompressionFile(String resourcesPath,String targetPath){
	        try {  
	            ZipInputStream Zin=new ZipInputStream(new FileInputStream(resourcesPath));//输入源zip路径  
	            BufferedInputStream Bin=new BufferedInputStream(Zin);  
	            String Parent=targetPath; //输出路径（文件夹目录）  
	            File fout=null;  
	            ZipEntry entry;  
	            try {  
	                while((entry = Zin.getNextEntry())!=null){  
	                	if(entry.isDirectory()){
	                		continue; 
	                	}
	                	fout=new File(Parent,entry.getName());  
	                    if(!fout.exists()){  
	                        (new File(fout.getParent())).mkdirs();  
	                    }  
	                    FileOutputStream out=new FileOutputStream(fout);  
	                    BufferedOutputStream Bout=new BufferedOutputStream(out);  
	                    int b;  
	                    while((b=Bin.read())!=-1){  
	                        Bout.write(b);  
	                    }  
	                    Bout.close();  
	                    out.close();  
	                    System.out.println(fout+"解压成功");      
	                }  
	                Bin.close();  
	                Zin.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        } catch (FileNotFoundException e) {  
	            e.printStackTrace();  
	        }  
	 }  
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		zipFile("F:\\Microsoft SQL Server","E:\\img1");
		long end = System.currentTimeMillis();
		// 2.39G 时间为：146s
		System.out.println((end-start)/1000);
		decompressionFile("E:\\img1\\Microsoft SQL Server.zip", "E:\\img1");
		long end1 = System.currentTimeMillis();
		System.out.println("智能解压："+(end1-end)/1000);
	}
}

