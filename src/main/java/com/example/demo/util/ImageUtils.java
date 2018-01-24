package com.example.demo.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileInputStream;

/**
 * 
 * 图像处理
 * 对图片进行压缩、水印、伸缩变换、透明处理、格式转换操作
 * @version 1.0 
 * @since JDK1.7 
 * @author zhangjingtao 
 * @company 洛阳艾克科技有限公司 
 * @copyright (c) 2018 LuoYang ARC Co'Ltd Inc. All rights reserved. 
 * @date 2018年1月17日 下午4:42:14
 */

public class ImageUtils {
	/**
	 * 默认透明度
	 */
	public static final float DEFAULT_QUALITY = 0.2125f ;
	/**
	 * 
	 * 添加水印
	 * 
	 * @param imgPath 待处理图片
	 * @param markPath 水印图片
	 * @param x  水印位于图片左上角的x值
	 * @param y  水印位于图片坐上角的y值
	 * @param alpha 水印透明度 0.1f ~ 1.0f
	 * @param destPath 文件存放的位置
	 * @throws Exception 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月17日 下午4:45:07
	 */
    public static void addWaterMark(String imgPath, String markPath, int x, int y, float alpha,String destPath) throws Exception{
        try {
               BufferedImage bufferedImage = addWaterMark(imgPath, markPath, x, y, alpha);
               ImageIO.write(bufferedImage, imageFormat(imgPath), new File(destPath));
           } catch (Exception e) {
               throw new RuntimeException("添加图片水印异常");
           }
    }
    /**
     * 
     * 水印处理
     * 
     * @param imgPath
     * @param markPath
     * @param x
     * @param y
     * @param alpha 
     * 
     * @author zhangjingtao 
     * @date 2018年1月17日 下午4:49:32
     */
    public static BufferedImage addWaterMark(String imgPath, String markPath, int x, int y, float alpha) throws Exception{
        BufferedImage targetImage = null;
        try {
            // 加载待处理图片文件
        	Image img = ImageIO.read(new File(imgPath));
            //创建目标图象文件
            targetImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = targetImage.createGraphics();
            g.drawImage(img, 0, 0, null);
            
            // 加载水印图片文件
            Image markImg = ImageIO.read(new File(markPath));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawImage(markImg, x, y, null);
            g.dispose();
        } catch (Exception e) {
            throw new RuntimeException("添加图片水印操作异常");
        }
        return targetImage;
       
    }
    /**
     * 
     * 获取图片的格式
     * 
     * @param imgPath
     * @return
     * @throws Exception 
     * 
     * @author zhangjingtao 
     * @date 2018年1月17日 下午4:57:46
     */
    public static String imageFormat(String imgPath)throws Exception{
        String[] filess = imgPath.split("\\\\");
        String[] formats = filess[filess.length - 1].split("\\.");
        return formats[formats.length - 1];
     }
    /**
     * 
     * 添加文字水印操作(物理存盘，使用默认格式) 
     * 
     * @param imgPath 待处理图片
     * @param text  水印文字
     * @param font  水印字体信息，不写默认为宋体
     * @param color 水印颜色
     * @param x   距离待处理图片左上角x值
     * @param y   距离待处理图片左上角y值
     * @param alpha  水印透明度 0.1f ~ 1.0f
     * @param destPath 文件存放路径 
     * @throws Exception 
     * 
     * @author zhangjingtao 
     * @date 2018年1月17日 下午5:36:53
     */
    public static void addTextMark(String imgPath, String text, Font font, Color color, float x, float y, float alpha,String destPath) throws Exception{
        try {
            BufferedImage bufferedImage = addTextMark(imgPath, text, font, color, x, y, alpha);
            ImageIO.write(bufferedImage, imageFormat(imgPath), new File(destPath));
        } catch (Exception e) {
            throw new RuntimeException("图片添加文字水印异常");
        }
    }
    /**
     * 
     * 添加文字水印操作，返回bufferedImage对象
     * 
     * @param imgPath
     * @param text
     * @param font
     * @param color
     * @param x
     * @param y
     * @param alpha
     * @return
     * @throws Exception 
     * 
     * @author zhangjingtao 
     * @date 2018年1月17日 下午5:41:23
     */
    public static BufferedImage addTextMark(String imgPath, String text, Font font, Color color, float x, float y, float alpha) throws Exception{
        BufferedImage targetImage = null;
        try {
            Font Dfont = (font == null) ? new Font("宋体", 20, 13) : font;    
            Image img = ImageIO.read(new File(imgPath));
            //创建目标图像文件
            targetImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = targetImage.createGraphics();
            g.drawImage(img, 0, 0, null);
            g.setColor(color);
            g.setFont(Dfont);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawString(text, x, y);
            g.dispose();
        } catch (Exception e) {
            throw new RuntimeException("添加文字水印操作异常");
        }
        return targetImage;
    }
    /**
     * 
     * 压缩图片操作(文件物理存盘,使用默认格式)
     * 
     * @param imgPath 待处理图片
     * @param quality 图片质量(0-1之間的float值)
     * @param width  输出图片的宽度    输入负数参数表示用原来图片宽
     * @param height  输出图片的高度 输入负数表示用原来的图片高
     * @param autoSize 是否等比缩放 true表示进行等比缩放 false表示不进行等比缩放
     * @param destPath  输出文件的路径地址
     * @throws Exception 
     * 
     * @author zhangjingtao 
     * @date 2018年1月17日 下午5:55:21
     */
    public static void compressImage(String imgPath,float quality,int width, int height, boolean autoSize,String destPath)throws Exception{
        try {
            BufferedImage bufferedImage = compressImage(imgPath, quality, width, height, autoSize);
            ImageIO.write(bufferedImage, imageFormat(imgPath), new File(destPath));
        } catch (Exception e) {
            throw new RuntimeException("图片压缩异常");
        }
        
    }
    /**
     * 
     * 压缩图片操作,返回BufferedImage对象
     * 压缩图片操作，返回bufferedImage对象
     * 
     * @param imgPath
     * @param quality
     * @param width
     * @param height
     * @param autoSize
     * @return
     * @throws Exception 
     * 
     * @author zhangjingtao 
     * @date 2018年1月17日 下午5:58:35
     */
    public static BufferedImage compressImage(String imgPath,float quality,int width, int height, boolean autoSize)throws Exception{
        BufferedImage targetImage = null;
        if(quality<0F||quality>1F){
            quality = DEFAULT_QUALITY;
        }
        try {
            Image img = ImageIO.read(new File(imgPath));
            //如果用户输入的图片参数合法则按用户定义的复制,负值参数表示执行默认值
            int newwidth =( width > 0 ) ? width : img.getWidth(null);
            //如果用户输入的图片参数合法则按用户定义的复制,负值参数表示执行默认值
            int newheight = ( height > 0 )? height: img.getHeight(null);    
           //如果是自适应大小则进行比例缩放
            if(autoSize){                                                    
                //为等比缩放计算输出的图片宽度及高度
                double Widthrate = ((double) img.getWidth(null)) / (double) width + 0.1;
                double heightrate = ((double) img.getHeight(null))/ (double) height + 0.1;
                double rate = Widthrate > heightrate ? Widthrate : heightrate;
                newwidth = (int) (((double) img.getWidth(null)) / rate);
                newheight = (int) (((double) img.getHeight(null)) / rate);
            }
            //创建目标图像文件
            targetImage = new BufferedImage(newwidth,newheight,BufferedImage.TYPE_INT_RGB);
            Graphics2D g = targetImage.createGraphics();
            g.drawImage(img, 0, 0, newwidth, newheight, null);
            //如果添加水印或者文字则继续下面操作,不添加的话直接返回目标文件----------------------
            g.dispose();
            
        } catch (Exception e) {
            throw new RuntimeException("图片压缩操作异常");
        }
        return targetImage;
    }
    
    /**
     * 
     * 图像黑白化
     * 图片黑白化操作(文件物理存盘,使用默认格式)
     * @param imgPath 待处理图片
     * @param destPath 存放地址
     * @throws Exception 
     * 
     * @author zhangjingtao 
     * @date 2018年1月17日 下午9:19:57
     */
    public static void imageGray(String imgPath, String destPath)throws Exception{
        imageGray(imgPath, imageFormat(imgPath), destPath);
    }
    /**
     *  图片黑白化操作(文件物理存盘,可自定义格式)
     * 
     * 
     * @param imgPath 待处理的图片
     * @param format  处理的格式
     * @param destPath  目标文件地址
     * @throws Exception 
     * 
     * @author zhangjingtao 
     * @date 2018年1月17日 下午9:21:39
     */
    public static void imageGray(String imgPath,String format, String destPath)throws Exception{
        try {
             BufferedImage bufferedImage = ImageIO.read(new File(imgPath));
             ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);            
             ColorConvertOp op = new ColorConvertOp(cs, null);  
             bufferedImage = op.filter(bufferedImage, null);
             ImageIO.write(bufferedImage, format , new File(destPath));
        } catch (Exception e) {
            throw new RuntimeException("图片灰白化异常");
        }
    }
    
    /**
     * 图片透明化操作(文件物理存盘,使用默认格式)
     * 
     * @param imgPath  待处理图片
     * @param destPath  目标文件地址
     * @throws Exception 
     * 
     * @author zhangjingtao 
     * @date 2018年1月17日 下午9:45:59
     */
    public static void imageLucency(String imgPath,String destPath)throws Exception{
        try {
            BufferedImage bufferedImage = imageLucency(imgPath);
            ImageIO.write(bufferedImage, imageFormat(imgPath), new File(destPath));
        } catch (Exception e) {
            throw new RuntimeException("图片透明化异常");
        }
    }
    /**
     * 
     *  图片透明化操作返回BufferedImage对象
     * 
     * @param imgPath
     * @return
     * @throws Exception 
     * 
     * @author zhangjingtao 
     * @date 2018年1月17日 下午9:47:18
     */
    public static BufferedImage imageLucency(String imgPath)throws Exception{
        BufferedImage targetImage = null;
        try {
            //读取图片   
            BufferedImage img = ImageIO.read(new FileInputStream(imgPath));
            //透明度
            int alpha = 0;    
            //执行透明化
            executeRGB(img, alpha);
            targetImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = targetImage.createGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();
        } catch (Exception e) {
            throw new RuntimeException("图片透明化执行异常");
        }
        return targetImage;
    }
    /**
     * 
     * 执行透明化的核心算法
     * 
     * @param img 图片对象
     * @param alpha 透明度
     * @throws Exception 
     * 
     * @author zhangjingtao 
     * @date 2018年1月17日 下午9:49:25
     */
    public static  void executeRGB(BufferedImage img, int alpha) throws Exception{
        int rgb = 0;//RGB值
        //x表示BufferedImage的x坐标，y表示BufferedImage的y坐标
        for(int x=img.getMinX();x<img.getWidth();x++){
            for(int y=img.getMinY();y<img.getHeight();y++){
                //获取点位的RGB值进行比较重新设定
                rgb = img.getRGB(x, y); 
                int R =(rgb & 0xff0000 ) >> 16 ; 
                int G= (rgb & 0xff00 ) >> 8 ; 
                int B= (rgb & 0xff ); 
                if(((255-R)<30) && ((255-G)<30) && ((255-B)<30)){ 
                    rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff); 
                    img.setRGB(x, y, rgb);
                }
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
    	String imgPath = "E:\\img\\abc.png";
//		String word ="不患寡而患不均";
//		Font f = new Font("TimesRoman",Font.BOLD,160);
//		Color c = new Color(80,156,60);
		//compressImage(imgPath, 0.2f, 600, 300, true,"E:\\img\\timg3.jpg");
    	//addTextMark(imgPath, word,f,c,150, 120, DEFAULT_QUALITY, "E:\\img\\timg2.jpg");
    	//imageGray(imgPath,"E:\\img\\timg3.jpg");
    	imageLucency(imgPath, "E:\\img\\timg3.jpg");
	}
}

