
package com.example.demo.util.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 
 * 导出excel文件 
 * 
 * @version 1.0 
 * @since JDK1.7 
 * @author zhangjingtao 
 * @company 洛阳艾克科技有限公司 
 * @copyright (c) 2018 LuoYang ARC Co'Ltd Inc. All rights reserved. 
 * @date 2018年1月18日 下午2:40:15
 */
public class ExcelExportUtil {
    private static Logger LOG = LoggerFactory.getLogger(ExcelExportUtil.class);

    /**
     * @param <T>
     * @param response
     * @param request
     * @param filename 导出的文件名
     * @param titles   标题列和列名的对应.column:列名,title标题名
     * @param records  记录
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    public static <T> void exportByRecord(HttpServletResponse response, HttpServletRequest request, String filename,
                                          List<Pair> titles, List<T> records) {
        try {
            exportByRecord(response, request, filename, new SheetData<T>(titles, records));
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            LOG.error(String.format("导出%s报表出错", filename), e);
        }
    }

    /**
     * @param response
     * @param request
     * @param filename   导出的文件名
     * @param sheetDatas 产生一个sheet需要的数据
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T> void exportByRecord(HttpServletResponse response, HttpServletRequest request, String filename,
                                          SheetData<T>... sheetDatas) throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {

        HSSFWorkbook wb = new HSSFWorkbook();

        // 标题行的style
        CellStyle titleCellStyle = wb.createCellStyle();
        titleCellStyle.setAlignment(CellStyle.ALIGN_CENTER); // 居中
        titleCellStyle.setWrapText(false); // 自动换行

        // 内容行的style
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 垂直居中
        cellStyle.setWrapText(false);

        // 设置红色字体
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.RED.index);

        // 多张sheet
        for (SheetData<T> sheetData : sheetDatas) {
            List<Pair> titles = sheetData.titles;

            List<T> records = sheetData.records;

            HSSFSheet sheet = wb.createSheet();

            int rowIndex = 0, cellIndex = 0;

            HSSFRow row = null;
            HSSFCell cell = null;

            // 创建标题行
            row = sheet.createRow(rowIndex);
            // 修改标题行高
            row.setHeight((short) 650);
            rowIndex++;

            for (Pair pair : titles) {
                // 修改每一列的列宽
                sheet.setColumnWidth((short) cellIndex, (short) (35.7 * 100));

                HSSFCellStyle cellStyleNew = wb.createCellStyle();
                cellStyleNew.setWrapText(true);

                cell = row.createCell(cellIndex);
                cell.setCellStyle(cellStyleNew); // 设置样式
                cellIndex++;
                String[] sourceStrArray = pair.title.split(",");
                if (sourceStrArray.length > 1) {
                    cell.setCellValue(sourceStrArray[0] + "\r\n" + sourceStrArray[1]);
                } else {
                    cell.setCellValue(pair.title);
                }
            }

            // 处理每一行
            for (T record : records) {

                row = sheet.createRow(rowIndex);
                row.setHeight((short) 450);
                rowIndex++;
                cellIndex = 0;
                int[] f = null;

                for (Pair pair : titles) {
                    cell = row.createCell(cellIndex);
                    cell.setCellStyle(cellStyle); // 设置样式
                    cellIndex++;
                    String methodName = pair.column;// userId/getUserId()

                    Object value = null;
                    String getMethodName = "get" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
                    Method method = record.getClass().getMethod(getMethodName);
                    Object obj = method.invoke(record);
                    value = obj;

                    // 根据数据类型做判断
                    if (value != null) {
                        if (methodName.equals("statusDc")) {
                            f = setFont(font, cell, value);// 根据考勤状态给单元格添加红色题样式
                        } else {
                            if (f != null) {
                                setAttendExceptionFond(f, font, cell, value, methodName);
                            } else {
                                cell.setCellValue(value.toString());

                            }
                        }
                    }
                }
            }
        }

        // 序号
        writeStream(filename, wb, response, request);
    }


    public static void exportByRecord1(HttpServletResponse response, HttpServletRequest request, String filename,
                                       List<Pair> titles, List<Map<String, String>> records) {
        try {
            exportByRecordMap(response, request, filename, new SheetData<Map<String, String>>(titles, records));
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            LOG.error(String.format("导出%s报表出错", filename), e);
        }
    }

    public static void exportByRecordMap(HttpServletResponse response, HttpServletRequest request, String filename,
                                         SheetData<Map<String, String>>... sheetDatas) throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {

        HSSFWorkbook wb = new HSSFWorkbook();

        // 标题行的style
        CellStyle titleCellStyle = wb.createCellStyle();
        titleCellStyle.setAlignment(CellStyle.ALIGN_CENTER); // 居中
        titleCellStyle.setWrapText(false); // 自动换行

        // 内容行的style
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 垂直居中
        cellStyle.setWrapText(false);

        // 设置红色字体
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.RED.index);

        // 多张sheet
        for (SheetData<Map<String, String>> sheetData : sheetDatas) {
            List<Pair> titles = sheetData.titles;

            List<Map<String, String>> records = sheetData.records;

            HSSFSheet sheet = wb.createSheet();

            int rowIndex = 0, cellIndex = 0;

            HSSFRow row = null;
            HSSFCell cell = null;

            // 创建标题行
            row = sheet.createRow(rowIndex);
            // 修改标题行高
            row.setHeight((short) 650);
            rowIndex++;

            for (Pair pair : titles) {
                // 修改每一列的列宽
                sheet.setColumnWidth((short) cellIndex, (short) (35.7 * 100));

                HSSFCellStyle cellStyleNew = wb.createCellStyle();
                cellStyleNew.setWrapText(true);

                cell = row.createCell(cellIndex);
                cell.setCellStyle(cellStyleNew); // 设置样式
                cellIndex++;
                String[] sourceStrArray = pair.title.split(",");
                if (sourceStrArray.length > 1) {
                    cell.setCellValue(sourceStrArray[0] + "\r\n" + sourceStrArray[1]);
                } else {
                    cell.setCellValue(pair.title);
                }
            }

            // 处理每一行
            for (Map<String, String> record : records) {

                row = sheet.createRow(rowIndex);
                row.setHeight((short) 450);
                rowIndex++;
                cellIndex = 0;
                int[] f = null;

                for (Pair pair : titles) {
                    cell = row.createCell(cellIndex);
                    cell.setCellStyle(cellStyle); // 设置样式
                    cellIndex++;
                    String methodName = pair.column;// userId/getUserId()

                    String value = record.get(methodName);
                    // 根据数据类型做判断
                    if (value != null) {
                        if (methodName.equals("statusDc")) {
                            f = setFont(font, cell, value);// 根据考勤状态给单元格添加红色题样式
                        } else {
                            if (f != null) {
                                setAttendExceptionFond(f, font, cell, value, methodName);
                            } else {
                                cell.setCellValue(value);

                            }
                        }
                    }
                }
            }
        }

        // 序号
        writeStream(filename, wb, response, request);
    }


    /**
     * 设置单元列为考勤状态的红色字体 以及 根据数据内容获得签到签退的异常数据
     */
    public static int[] setFont(HSSFFont font, HSSFCell cell, Object value) {
        HSSFRichTextString richString = new HSSFRichTextString(value.toString());
        String[] str;
        int f[] = {-1, -1, -1, -1};
        try {
            str = value.toString().split("/");
            int len = value.toString().length();
            int a = value.toString().indexOf("/");

            if (setArray(str[0], f, 0)) {// 签到状态设置红色字体

                richString.applyFont(0, a, font);

            }
            if (setArray(str[1], f, 1)) {

                richString.applyFont(a + 1, len, font);

            }

            cell.setCellValue(richString);
            return f;
        } catch (Exception e) {
            cell.setCellValue(richString);
            return null;
        }

    }

    /**
     * 获得签到签退是的考勤状态 数据结果存储到数组中 1为异常 0为正常 数组初始值设为-1 c值 0为判断签到的数据 1为判断签退时的数据
     */
    public static boolean setArray(String str, int[] a, int c) {
        boolean f = false;
        switch (c) {
            case 0:
                if ((str.indexOf("迟到") > -1) && (str.indexOf("偏离") > -1)) {
                    a[0] = 1;
                    a[1] = 1;
                    f = true;
                } else {
                    if ((str.indexOf("迟到") > -1)) {
                        a[0] = 1;
                        a[1] = 0;
                        f = true;
                    } else {
                        if ((str.indexOf("偏离") > -1)) {
                            a[0] = 0;
                            a[1] = 1;
                            f = true;

                        } else {
                            a[0] = a[1] = 0;
                            f = false;
                        }
                    }
                }
                break;
            case 1:
                if ((str.indexOf("早退") > -1) && (str.indexOf("偏离") > -1)) {
                    a[2] = 1;
                    a[3] = 1;
                    f = true;
                } else {
                    if ((str.indexOf("早退") > -1)) {
                        a[2] = 1;
                        a[3] = 0;
                        f = true;
                    } else {
                        if ((str.indexOf("偏离") > -1)) {
                            a[2] = 0;
                            a[3] = 1;
                            f = true;
                        } else {
                            a[2] = a[3] = 0;
                            f = false;
                        }
                    }
                }
                break;
        }
        return f;

    }

    /**
     * 根据参数给签到签退时间地点设置红色字体
     */
    public static void setAttendExceptionFond(int[] a, HSSFFont f, HSSFCell cell, Object value, String methodName) {
        String str = value.toString();
        int b = str.length();
        HSSFRichTextString richString = new HSSFRichTextString(str);
        if (methodName.equals("signInTime")) {
            if (a[0] == 1) {
                richString.applyFont(0, b, f);
            }
        }
        if (methodName.equals("signInAddress")) {
            if (a[1] == 1) {
                richString.applyFont(0, b, f);
            }
        }
        if (methodName.equals("signOutTime")) {
            if (a[2] == 1) {
                richString.applyFont(0, b, f);
            }
        }
        if (methodName.equals("signOutAddress")) {
            if (a[3] == 1) {
                richString.applyFont(0, b, f);
            }
        }
        if (methodName.equals("absenteeismDc")) {
            if (!str.equals("——")) {
                richString.applyFont(0, b, f);
            }

        }
        cell.setCellValue(richString);
    }

    /**
     * 写到输出流
     */
    private static void writeStream(String filename, HSSFWorkbook wb, HttpServletResponse response,
                                    HttpServletRequest request) {

        try {
            String agent = request.getHeader("USER-AGENT");

            filename += ".xls";

            filename.replaceAll("/", "-");
            // filename = new String(filename.getBytes("gbk"),"ISO8859_1");

            if (agent.toLowerCase().indexOf("firefox") > 0) {
                filename = new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            } else {
                filename = URLEncoder.encode(filename, "UTF-8");
            }

            response.reset();
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.setContentType("application/octet-stream;charset=UTF-8");

            //更新完后，设定cookie，用于页面判断更新完成后的标志
            Cookie status = new Cookie("updateStatus", "success");
            status.setMaxAge(600);
            response.addCookie(status);//添加cookie操作必须在写出文件前，如果写在后面，随着数据量增大时cookie无法写入

            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            LOG.error("写入到输出流出错", e);
        }

    }

    /**
     * 标题列和列名的对应
     */
    public static class Pair {
        public String column;// 列名

        public String title;// 标题

        public Pair(String column, String title) {
            this.column = column;

            this.title = title;

        }
    }

    /**
     * 创建一个sheet需要的数据
     */
    public static class SheetData<T> {
        public List<Pair> titles;
        public List<T> records;

        public SheetData(List<Pair> titles, List<T> records) {
            this.titles = titles;

            this.records = records;
        }
    }
    
    public static void main(String[] args) {
    	/**
    	 * fileName 导出excel名称
    	 * titels excel表头信息
    	 * list 数据
    	 */
    	//ExcelExportUtil.exportByRecord(response, request, fileName, titles, list);
	}
}
