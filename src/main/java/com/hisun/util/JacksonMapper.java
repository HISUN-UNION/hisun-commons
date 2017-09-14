package com.hisun.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 *<p>类名称：JacksonMapper</p>
 *<p>类描述: </p>
 *<p>公司：湖南海数互联信息技术有限公司</p>
 *@创建人：zhouying
 *@创建时间：2014-11-11 上午11:26:22
 *@创建人联系方式：zhouying@30wish.net
 *@version
 */
public class JacksonMapper {

	private static ObjectMapper mapper = new  ObjectMapper();
	
	private JacksonMapper(){}
	
	public static ObjectMapper newInstance(){
		return mapper;
	}
}
