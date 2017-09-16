package com.hisun.util;

/**
 * Created by zhouying on 2017/9/16.
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    public static String transNull(String s){
        return s==null?"":s;
    }
}
