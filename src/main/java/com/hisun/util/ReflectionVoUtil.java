package com.hisun.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhouying on 2017/12/8.
 */
public class ReflectionVoUtil {

    public static Map<String,Object> map(Object vo) throws Exception{
        Map<String,Object> resultMap = new HashMap<String,Object>();
        Class clz = vo.getClass();
        Field[] fields = clz.getDeclaredFields();
        for(Field field : fields){
            String name = field.getName();
            Method method = findGetMethod(clz,name);
            Object object = method.invoke(vo);

//            String genericType = field.getGenericType().toString();
//            if(genericType.equals("class java.lang.String")){
//                Method method = findGetMethod(clz,name);
//                value = StringUtils.trimNullCharacter2Empty((String)method.invoke(vo));
//            }else if(genericType.equals("class java.util.Date")){
//                Method method = findGetMethod(clz,name);
//                Date date = (Date)method.invoke(vo);
//                if(date!=null){
//                    value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
//                }
//            }else {
//                Method method = findGetMethod(clz,name);
//                value = StringUtils.trimNullCharacter2Empty(String.valueOf(method.invoke(vo)));
//            }
//            resultMap.put(name,value);
            resultMap.put(name,object);
        }
        return resultMap;
    }

    public static Object vo(Class clz,Map<String,Object> map)throws Exception{
        Object vo = clz.newInstance();
        for(Iterator<String> it = map.keySet().iterator();it.hasNext();){
            String name = it.next();
            Object value = map.get(name);
            Field field = clz.getDeclaredField(name);
            if(field.isAccessible()){
                field.set(vo,value);
            }else{
                field.setAccessible(true);
                field.set(vo,value);
                field.setAccessible(false);
            }
        }
        return vo;
    }


    public static Method findGetMethod(Class clz,String name)throws Exception{
        return (Method) clz.getMethod("get" + getMethodName(name));
    }


    private static String getMethodName(String fieldName) throws Exception{
        byte[] items = fieldName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

}
