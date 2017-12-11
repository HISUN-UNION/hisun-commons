package com.hisun.util;

/**
 * Created by zhouying on 2017/9/16.
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils{

    public static String trimNull2Empty(String s){
        return s==null?"":s;
    }

    public static String trimNullCharacter2Empty(String s){
        if(s==null){
            return "";
        }else if(s.toLowerCase().equals("null")){
            return "";
        }
        return s;
    }

    public static String trimBlankCharacter2Empty(String str) {
        if (str == null) {
            return "";
        } else {
            //去掉换行符
            str = str.replaceAll("[\\s\b\r)]*", "");
            str = str.replaceAll("[\u0007]*","");
            //去掉全角空格
            str = org.apache.commons.lang3.StringUtils.trim(str.replace((char) 12288, ' '));
            return str;
        }
    }
}
