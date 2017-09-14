package com.hisun.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

/**
 * 
 * <p>Title: JacksonUtil</p>
 * <p>Description: Json转换类</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 湖南海数互联信息技术有限公司</p>
 * @author Jason
 * @email jason4j@qq.com
 * @date 2015年7月6日 下午4:30:25 
 * @version
 */
public class JacksonUtil {

	private static final Logger logger = Logger.getLogger(JacksonUtil.class);

	private ObjectMapper mapper;

	public JacksonUtil() {
		this(null);
	}

	public JacksonUtil(Include include) {
		mapper = new ObjectMapper();
		// 设置输出时包含属性的风格
		if (include != null) {
			mapper.setSerializationInclusion(include);
		}
		// 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	/**
	 * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper,建议在外部接口中使用.
	 */
	public static JacksonUtil nonEmptyMapper() {
		return new JacksonUtil(Include.NON_EMPTY);
	}

	/**
	 * 创建只输出初始值被改变的属性到Json字符串的Mapper, 最节约的存储方式，建议在内部接口中使用。
	 */
	public static JacksonUtil nonDefaultMapper() {
		return new JacksonUtil(Include.NON_DEFAULT);
	}

	public JacksonUtil(String dateType, String format) {
		mapper = new ObjectMapper();

		mapper = mapper.setDateFormat(new SimpleDateFormat(format));
	}

	public static JacksonUtil simpleDateFormatMapper() {
		return new JacksonUtil("SimpleDateFormat", "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * Object可以是POJO，也可以是Collection或数组。 如果对象为Null, 返回"null". 如果集合为空集合, 返回"[]".
	 */
	public String toJson(Object object) {

		try {
			return mapper.writeValueAsString(object);
		} catch (IOException e) {
			logger.warn("write to json string error:" + object, e);
			return null;
		}
	}

	/**
	 * 反序列化POJO或简单Collection如List<String>.
	 * 
	 * 如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合.
	 * 
	 * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String, JavaType)
	 * 
	 * @see #fromJson(String, JavaType)
	 */
	public <T> T fromJson(String jsonString, Class<T> clazz) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}

		try {
			return mapper.readValue(jsonString, clazz);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 反序列化复杂Collection如List<Bean>,
	 * 先使用createCollectionType()或contructMapType()构造类型, 然后调用本函数.
	 * 
	 * @see #createCollectionType(Class, Class...)
	 */
	@SuppressWarnings("unchecked")
	public <T> T fromJson(String jsonString, JavaType javaType) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}

		try {
			return (T) mapper.readValue(jsonString, javaType);
		} catch (IOException e) {
			logger.warn("parse json string error:" + jsonString, e);
			return null;
		}
	}

	/**
	 * 构造Collection类型.
	 */
	public JavaType contructCollectionType(
			Class<? extends Collection> collectionClass,
			Class<?> elementClass) {
		return mapper.getTypeFactory().constructCollectionType(collectionClass,
				elementClass);
	}

	public <T> T fromJson(String jsonStr,Class<? extends Collection> collectionClass, Class<?> elementClass){
		JavaType javaType = contructCollectionType(collectionClass, elementClass);
		return fromJson(jsonStr,javaType);
	}

	/**
	 * 构造Map类型.
	 */
	public JavaType contructMapType(Class<? extends Map<?, ?>> mapClass,
			Class<?> keyClass, Class<?> valueClass) {
		return mapper.getTypeFactory().constructMapType(mapClass, keyClass,
				valueClass);
	}

	/**
	 * 当JSON里只含有Bean的部分屬性時，更新一個已存在Bean，只覆蓋該部分的屬性.
	 */
	public void update(String jsonString, Object object) {
		try {
			mapper.readerForUpdating(object).readValue(jsonString);
		} catch (JsonProcessingException e) {
			logger.warn("update json string:" + jsonString + " to object:"
					+ object + " error.", e);
		} catch (IOException e) {
			logger.warn("update json string:" + jsonString + " to object:"
					+ object + " error.", e);
		}
	}

	/**
	 * 取出Mapper做进一步的设置或使用其他序列化API.
	 */
	public ObjectMapper getMapper() {
		return mapper;
	}

}
