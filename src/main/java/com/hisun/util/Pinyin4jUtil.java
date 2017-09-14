package com.hisun.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

/**
 * <p>Title: Pinyin4jUtil.java </p>
 * <p>Package net.wish30.util </p>
 * <p>Description: TODO</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 湖南海数互联信息技术有限公司</p>
 * @author Jason
 * @email jason4j@qq.com
 * @date 2015年10月27日 下午3:49:46 
 * @version 
 */
public class Pinyin4jUtil {

	//转换一个字符串
    public static String getStringPinYin(String str) throws Exception{
    	StringBuilder sb = new StringBuilder();  
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();  
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
		for (int i = 0; i < str.length(); i++) {  
            char word = str.charAt(i);  
            String[] pinyinArray =PinyinHelper.toHanyuPinyinStringArray(word,format);
            if (pinyinArray != null) {  
            	sb.append(pinyinArray[0].charAt(0));// 只取一个发音，如果是多音字，仅取第一个发音
            }else{
            	// 如果str.charAt(i)非汉字，则保持原样
                sb.append(word);
            }
		}
		return sb.toString();
    }
}
