package com.hisun.util;

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ValidateUtil {
	
	public static Map<String,String> validAll(Object obj){
		Map<String, String> map = new HashMap<String, String>();
		LocalValidatorFactoryBean b = ApplicationContextUtil.getBean("validatorFactory",LocalValidatorFactoryBean.class);
		Validator v = b.getValidator();
		//Validator validation = vf.getValidator();
		Set<ConstraintViolation<Object>> set = v.validate(obj);
		Iterator<ConstraintViolation<Object>> it = set.iterator();
		while(it.hasNext()){
			ConstraintViolation<Object> tmp = it.next();
			map.put(tmp.getPropertyPath().toString(), tmp.getMessage());
		}
		return map;
	}
}
