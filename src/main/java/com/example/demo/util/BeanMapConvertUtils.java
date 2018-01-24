package com.example.demo.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * bean和 map的相互转换 
 * 
 * @version 1.0 
 * @since JDK1.7 
 * @date 2018年1月18日 下午5:22:06
 */
public class BeanMapConvertUtils {

	public static Map<String,Object> bean2MapObject(Object object){
        if(object == null){
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    //得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(object);

                    map.put(key, value);
                }
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        return map;
    }
	/**
	 * 
	 * map转换为bean
	 * 
	 * @param map
	 * @param object
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月18日 下午5:31:13
	 */
	public static Object map2Bean(Map map,Object object){
        if(map == null || object == null){
            return null;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (map.containsKey(key)) {
                    Object value = map.get(key);
                    // 得到property对应的setter方法
                    Method setter = property.getWriteMethod();
                    setter.invoke(object, value);
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }
}

