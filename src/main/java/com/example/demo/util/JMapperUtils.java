package com.example.demo.util;

import com.googlecode.jmapper.JMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类型映射转换
 * 1.使用前需要引入相关jar包 ：jmapper-core
 * 2.源类上需要引入@JMap(attributes = {"fileId"}, classes = {destionation.class}) ||  @JGlobalMap(excluded = {""})
 * @version 1.0 
 * @date 2018年1月24日 下午2:51:59
 */

public class JMapperUtils {
	/**
	 * 
	 *  集合转换
	 * 
	 * @param sources  被转换对象
	 * @param destination  目标类
	 * @param source    源类
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月24日 下午3:04:04
	 */
	public static <S,D> List<D> getDestinations(List<S> sources,Class<D> destination,Class<S> source){
		JMapper<D,S> principalMapper = new JMapper<>(destination,source);
		return sources.stream().map(principalMapper::getDestination).collect(Collectors.toList());
	}
	/**
	 * 
	 * 单个对象转换
	 * 
	 * @param sourceObject
	 * @param destionation
	 * @param source
	 * @return 
	 * 
	 * @author zhangjingtao 
	 * @date 2018年1月24日 下午3:07:25
	 */
	public static <S,D> D getDestination (S sourceObject,Class<D> destionation,Class<S> source) {
		JMapper<D,S> principalMapper = new JMapper<>(destionation,source);
		return principalMapper.getDestination(sourceObject);
	}
}

