package com.hisun.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * 主要作用让不是Spring管理的类能通过这个类getBean获取对应的Bean
* @ClassName: ApplicationContextUtil 
* @Description: 
* @author lyk
* @date 2015年3月19日
 */
public class ApplicationContextUtil implements ApplicationContextAware{
	
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public static <T> T getBean(Class<T> cls){
		return applicationContext.getBean(cls);
	}
	
	public static <T> T getBean(String name, Class<T> cls){
		return applicationContext.getBean(name, cls);
	}
	
	public static <T> T getBean(String name){
		return (T)applicationContext.getBean(name);
	}
}
