package com.example.demo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.entity.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * Json工具类  
 *   此工具类使用alibaba fastjson
 * @version 1.0 
 * @since JDK1.7 
 * @date 2018年1月24日 下午3:53:18
 */

public class JsonUtils {
    private static final SerializeConfig config;

    static {
        config = new SerializeConfig();
        // compatible with the java.util.Date and the java.sql.Date
        config.put(java.util.Date.class, new JSONLibDataFormatSerializer());
        config.put(java.sql.Date.class, new JSONLibDataFormatSerializer());
    }

    /**
     * set the default value of the object which the value is null
     */
    private static final SerializerFeature[] features = {
            SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteNullListAsEmpty,
            SerializerFeature.WriteNullBooleanAsFalse,
            SerializerFeature.WriteNullStringAsEmpty,
    };

    /**
     * list to json
     *
     * @param list
     *          the list that will transform to json string
     * @return
     *          the json string of list transform
     */
    public static String list2json(List list) {
        return JSON.toJSONString(list);
    }

    /**
     * map to json
     * @param map
     *          the map that will transform to json string
     * @return
     *          the json string of map transform
     */
    public static String map2json(Map map) {
        return JSONObject.toJSONString(map);
    }

    /**
     * object array to json
     *
     * @param objects
     *          the object array that will transform to json string
     * @return
     *          the json string of array transform
     */
    public static String array2json(Object[] objects) {
        return JSON.toJSONString(objects);
    }

    /**
     * object to json
     *
     * @param object
     *          the object that will transform to json string
     * @return
     *          the json string of object
     */
    public static String object2json(Object object) {
        return JSON.toJSONString(object, config, features);
    }


    /**
     * json to list
     *
     * @param json
     *          the json string that will transform to list
     * @param clazz
     *          the class of the list's element
     * @param <T>
     *          the generic of the class
     * @return
     *          the list that json string transform
     */
    public static <T> List<T> json2list(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    /**
     * json to map
     *
     * @param json
     *          json string that will transform to map
     * @return
     *          the map fo json string
     */
    public static Map json2map(String json) {
        return JSONObject.parseObject(json);
    }


    /**
     * json string to object array
     *
     * @param json
     *          the json string will transform to object array
     * @param clazz
     *          the class of the json will transform
     * @param ts
     *          the real object array
     * @param <T>
     *          the real object
     * @return
     *          the object array of the json string
     *
     * @param json
     * @param clazz
     * @param ts
     * @param <T>
     * @return
     */
    public static <T> T[] json2array(String json, Class<T> clazz, T[] ts) {
        return JSON.parseArray(json, clazz).toArray(ts);
    }

    /**
     * json string to object
     *
     * @param json
     *          the json string that will transform to object
     * @param clazz
     *          the class that json will transform
     * @param <T>
     *          the object class
     * @return
     *          the object of json string
     */
    public static <T> Object json2object(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }
    
    public static void main(String[] args) {
		List<Student> students  = new ArrayList<>();
		students.add(new Student(1,"张三","男",18));
		students.add(new Student(2,"sally","男",16));
		students.add(new Student(3,"赵四","男",25));
		students.add(new Student(4,"王五","男",22));
		String json = JsonUtils.list2json(students);
		System.out.println(json);
	}
}

