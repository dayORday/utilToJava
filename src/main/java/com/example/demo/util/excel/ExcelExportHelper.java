package com.example.demo.util.excel;

import com.example.demo.entity.Student;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 
 * excel导出通用类 
 * 
 * @version 1.0 
 * @since JDK1.7 
 * @date 2018年1月18日 下午12:04:19
 */
public class ExcelExportHelper {
	/**默认日期格式*/
	private  String DATE_PATTERN = "yyyy-mm-dd";
	/**照片宽度*/
	private int IMAGE_WIDTH = 30;
	/**照片高度*/
	private int IMAGE_HEIGHT = 50;
	/** 单元格的最大宽度 */
	private int[] maxWidth;
	/** 
	 * 单页支持最多数据列：超过65534会出错
	 * 若数据列多余65534则需要通过MORE_EXCEL_FLAG、MORE_SHEET_FLAG来区别生成多个Excel、还是sheet
	 */
	private int maxRowCount = 2500;
	
	/** 大量数据，多个Excel标识---0001 */
	private String  MORE_EXCEL_FLAG = "0001";
	
	/** 大量数据，多个sheet标识---0001 */
	private String MORE_SHEET_FLAG = "0002";
	
	/**
	 * 默认构造函数 
	 */
	public ExcelExportHelper(){
	}
	
	/**
	 * @param datePattern 指定的时间格式
	 */
	public ExcelExportHelper(String datePattern){
		this.DATE_PATTERN = datePattern;
	}
	
	/**
	 * @param imageWidth 
	 * 					指定图片的宽度
	 * @param imageHeight
	 * 				           指定图片的高度
	 */
	public ExcelExportHelper(int imageWidth,int imageHeight){
		this.IMAGE_HEIGHT = imageHeight;
		this.IMAGE_WIDTH = imageWidth;
	}
	
	/**
	 * @param datePatter 
	 * 					指定时间格式
	 * @param   //imageWidth
	 * 					指定图片的宽度
	 * @param imageHeight 
	 * 					指定图片的高度
	 */
	public ExcelExportHelper(String datePatter,int imageWidht,int imageHeight){
		this.DATE_PATTERN = datePatter;
		this.IMAGE_HEIGHT = imageHeight;
		this.IMAGE_WIDTH = imageWidht;
	}
	/**
	 * 根据提供表头 header ，数据列 excelList 生成 Excel,如有图片请转换为byte[]<
	 * header、excelList中的Bean必须对应（javaBean的属性顺序）：如下<br>
	 * header：姓名、年龄、性别、班级<br>
	 * Bean：name、age、sex、class<br>
	 * 
	 * @param header 表格属性列名数组
	 * @param excelList  需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的 javabean
	 *          属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param sheetTitle  表格标题名
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 下午1:08:02
	 */
	public  HSSFWorkbook exportExcel(String[] header,List<?> excelList,String sheetTitle){
		//生成一个Excel
		HSSFWorkbook book = new HSSFWorkbook();  
		//生成一个表格
		sheetTitle = getSheetTitle(sheetTitle);   
		//判断、设置sheetTitle
		HSSFSheet sheet = book.createSheet(sheetTitle);
		
		//设置Excel里面数据
		setExcelContentData(book,sheet,header,excelList);
		//保存excel文件
		saveExcel(book,"E:\\img","avb");
		System.out.println("——————————————————ExcelExportHelper:Excel生成成功...");
		
		return book;
	}
	/**
	 * 
	 * 设置sheet的title，若为空则为yyyyMMddHH24mmss
	 * 
	 * @param sheetTitle
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 下午1:20:32
	 */
	private static String  getSheetTitle(String sheetTitle){
		String data = null;
		if (sheetTitle != null && !"".equals(sheetTitle)) {
			data = sheetTitle;
		} else {
			LocalDateTime localDateTime = LocalDateTime.now();
			DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyyMMddHH24mmss");
			data = localDateTime.format(formater);
		}
		return  data;
	}
	/**
	 * 
	 * 填充excel内容
	 * 
	 * @param book  工作簿
	 * @param sheet 表
	 * @param header 表头
	 * @param excelList  填充数据
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 下午1:25:35
	 */
	private void setExcelContentData(HSSFWorkbook book,HSSFSheet sheet,String[] header,List<?> excelList) {
		//设置列头样式(居中、变粗、蓝色)
		HSSFCellStyle headerStyle = book.createCellStyle();
		setHeaderStyle(headerStyle, book);

		// 设置单元格样式
		HSSFCellStyle cellStyle = book.createCellStyle();
		setCellStyle(cellStyle, book);

		// 创建头部
		HSSFRow row = createHeader(sheet, headerStyle, header);

		// 画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

		
		int index = 0;
		/* 避免在迭代过程中产生的新对象太多，这里将循环内部变量全部移出来 */
		Object t = null;    
		HSSFCell cell = null;
		Field field = null;
		String fieldName = null;
		String getMethodName = null;
		Class tCls = null;
		Method getMethod = null;
		Object value = null;
		// 遍历集合数据，产生数据行
		Iterator<?> it = excelList.iterator();
		maxWidth = new int[header.length];   //初始化单元格宽度
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			// 设置数据列
			t = it.next();
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			Field[] fields = t.getClass().getDeclaredFields();
			for (short i = 0; i < fields.length; i++) {
				cell = row.createCell(i);
				cell.setCellStyle(cellStyle);
				field = fields[i];
				fieldName = field.getName();
				getMethodName = "get"+ fieldName.substring(0, 1).toUpperCase()+ fieldName.substring(1);  //构建getter方法
				try {
					tCls = t.getClass();
					getMethod = tCls.getMethod(getMethodName,new Class[] {});
				    value = (Object) getMethod.invoke(t, new Object[] {});
					// 将value设置当单元格指定位置
					setCellData(row, index, i, value, cell, sheet, patriarch, book);
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
					System.out.println("——————————————————创建Excel数据列表时出错。method:setDataRow,message："+e.getMessage());
				} catch (SecurityException e) {
					e.printStackTrace();
					System.out.println("——————————————————创建Excel数据列表时出错。method:setDataRow,message："+e.getMessage());
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					System.out.println("——————————————————创建Excel数据列表时出错。method:setDataRow,message："+e.getMessage());
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					System.out.println("——————————————————创建Excel数据列表时出错。method:setDataRow,message："+e.getMessage());
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					System.out.println("——————————————————创建Excel数据列表时出错。method:setDataRow,message："+e.getMessage());
				}
			}
		}
		
		System.out.println("-------------------------填充Excel数据成功..........");
	}
	/**
	 * 
	 * 设置表头样式  字体居中、变粗、蓝色、12号
	 * 
	 * @param headerStyle
	 * @param book 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 下午1:30:04
	 */
	private void setHeaderStyle(HSSFCellStyle headerStyle,HSSFWorkbook book) {
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);   //水平居中
		headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中 
		//设置字体
		HSSFFont font = book.createFont();
		font.setFontHeightInPoints((short) 12);     //字号：12号
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);   //变粗
		font.setColor(HSSFColor.BLUE.index);   //蓝色
		
		headerStyle.setFont(font);
	}
	
	/**
	 * 
	 * 设置单元格样式
	 * 
	 * @param cellStyle
	 * @param book 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 下午1:32:16
	 */
	private void setCellStyle(HSSFCellStyle cellStyle, HSSFWorkbook book) {
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);   //水平居中
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中 
		HSSFFont font = book.createFont();
		font.setFontHeightInPoints((short)12);
		cellStyle.setFont(font);
	}
	/**
	 * 
	 * 依据头部样式以及数据 创建表头
	 * 
	 * @param sheet
	 * @param headerStyle 表头样式
	 * @param header 表数据
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 下午1:33:58
	 */
	private HSSFRow createHeader(HSSFSheet sheet,HSSFCellStyle headerStyle,
			String[] header) {
		HSSFRow headRow = sheet.createRow(0);
		headRow.setHeightInPoints((short)(50));   //设置头部高度
		//添加数据
		HSSFCell cell = null;
		for(int i = 0 ; i < header.length ; i++){
			cell = headRow.createCell(i);
			cell.setCellStyle(headerStyle);
			HSSFRichTextString text = new HSSFRichTextString(header[i]);
			cell.setCellValue(text);
		}
		
		return headRow;
	}
	/**
	 * 
	 * 设置单元格数据
	 * 
	 * @param row
	 * @param index
	 * @param i
	 * @param value
	 * @param cell
	 * @param sheet
	 * @param patriarch 顶级画板 用于实现突破
	 * @param book 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 下午1:41:41
	 */
	private void setCellData(HSSFRow row, int index ,int i ,Object value,HSSFCell cell,HSSFSheet sheet,HSSFPatriarch patriarch,HSSFWorkbook book) {
		String textValue = null; 
		if (value instanceof Date) {    //为日期设置时间格式
			Date date = (Date) value;
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
			textValue = sdf.format(date);  
		}
		if(value instanceof byte[]){   //byte为图片
			//设置图片单元格宽度、高度
			row.setHeightInPoints((short)(IMAGE_HEIGHT * 10));
			sheet.setColumnWidth(i, IMAGE_WIDTH * 256);
		    HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255,(short) i, index, (short) i, index);   
	        anchor.setAnchorType(3);   
	        //插入图片  
	        byte[] bsValue = (byte[]) value;
	        patriarch.createPicture(anchor, book.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG)); 
		}
		else{   //其余全部当做字符处理
			if(value != null){
				textValue = String.valueOf(value);
			}
			else{
				textValue = "";
			}
		}
		// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
		if (textValue != null) {
			Pattern p = Pattern.compile("^//d+(//.//d+)?$");
			Matcher matcher = p.matcher(textValue);
			
			//设置单元格宽度，是文字能够全部显示
			setCellMaxWidth(textValue,i);
			sheet.setColumnWidth(i, maxWidth[i]);    //设置单元格宽度
			row.setHeightInPoints((short)(20));   //设置单元格高度
			if (matcher.matches()) {
				// 是数字当作double处理
				cell.setCellValue(Double.parseDouble(textValue));
			} else {
				cell.setCellValue(textValue);
			}
		}
	}
	/**
	 * 
	 * 根据当前列的字体长度，更新当前列的宽度 
	 * 
	 * @param textValue 值
	 * @param i  某一列
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 下午1:40:18
	 */
	private void setCellMaxWidth(String textValue,int i ) {
		int size = textValue.length();
		int width = (size + 6) * 256;
		if(maxWidth[i] <= width){
			maxWidth[i] = width;
		}
	}
	/**
	 * 
	 * 保存excel文件
	 * 
	 * @param book
	 * @param filePath
	 * @param fileName 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 下午2:11:25
	 */
	private void saveExcel(HSSFWorkbook book, String filePath, String fileName) {
		//检测保存路劲是否存在，不存在则新建
		checkFilePathIsExist(filePath);
		//将Excel保存至指定目录下
		fileName = getFileName(fileName);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePath + "\\" + fileName + ".xls");
			book.write(out); 
			System.out.println("——————————————————保存Excel文件成功，保存路径：" + filePath + "\\" + fileName + ".xls");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("——————————————————保存Excel文件失败。exportExcelForListAndSave,message："+e.getMessage());
		}finally{
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 
	 * 检查文件是否存在 
	 * 
	 * @param filePath 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 下午2:12:12
	 */
	private void checkFilePathIsExist(String filePath) {
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	/**
	 * 
	 * 获取文件的名称 若为空，则规则为：yyyyMMddHH24mmss+6位随机数
	 * 
	 * @param fileName
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 下午2:12:55
	 */
	private String getFileName(String fileName) {
		if(fileName == null || "".equals(fileName)){
			//日期
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH24mmss");
			//随机数
			Random random = new Random();
			fileName = sdf.format(date) + String.valueOf(Math.abs(random.nextInt() * 1000000));
		}
		return fileName;
	}
	
	public static void main(String[] args) {
//		System.out.println(getSheetTitle(""));
		String[] header = new String[4];
		header[0]="编号";
		header[1]="姓名";
		header[2]="性别";
		header[3]="年龄";
		List<Student> excelList = new ArrayList<>();
		excelList.add(new Student(1,"张三","男",18));
		excelList.add(new Student(2,"李四","男",19));
		excelList.add(new Student(3,"王二","男",20));
		excelList.add(new Student(4,"赵倩","男",20));
		excelList.add(new Student(5,"孙俪","男",29));
		String sheetTitle = "清华附中二年级学生统计表";
		new ExcelExportHelper().exportExcel(header,excelList,sheetTitle);
		System.out.println("---------");
		
	}

}

