package com.hisun.util;

import com.google.common.collect.Maps;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: HttpClientUtil.java </p>
 * <p>Package net.wish30.util </p>
 * <p>Description: HttpClient工具类get/post</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 湖南海数互联信息技术有限公司</p>
 *
 * @author Jason
 * @email jason4j@qq.com
 * @date 2015年4月30日 上午9:14:02
 */
public class HttpClientUtil {

    private static final Logger logger = Logger.getLogger(HttpClientUtil.class);

    //传输超时60s
    private static final int SOCKE_TTIME_OUT = 60000;

    //请求超时60s
    private static final int CONNECT_TIME_OUT = 60000;

    /**
     * 基于HttpClient 4.5的通用POST方法 无状态返回 无Cookie
     *
     * @param url       提交的URL
     * @param paramsMap 提交<参数，值>Map
     * @return 提交响应
     */
    public static <T> T post(String url, Map<String, ?> paramsMap, Class<T> clazz, int sockeTimeOut) {

        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(sockeTimeOut).setConnectTimeout(CONNECT_TIME_OUT).build();//设置请求和传输超时时间
            if (paramsMap != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, ?> param : paramsMap.entrySet()) {
                    if (null == param.getValue()) {
                        NameValuePair pair = new BasicNameValuePair(param.getKey(), null);
                        paramList.add(pair);
                    } else if (checkType(param.getValue())) {
                        String strs[] = (String[]) param.getValue();
                        for (String str : strs) {
                            NameValuePair pair = new BasicNameValuePair(param.getKey(), str);
                            paramList.add(pair);
                        }
                    } else {
                        NameValuePair pair = new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue()));
                        paramList.add(pair);
                    }
                }
                method.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            }
            method.setConfig(requestConfig);
            response = client.execute(method);
            //client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            logger.error(e, e);
            throw new RuntimeException(e);
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                logger.error(e, e);
                throw new RuntimeException(e);
            }
        }
        if (checkString(clazz)) {
            return (T) responseText;
        } else {
            return JacksonUtil.nonDefaultMapper().fromJson(responseText, clazz);
        }
    }

    /**
     * 基于HttpClient 4.5的通用POST方法 无状态返回 无Cookie
     *
     * @param url       提交的URL
     * @param paramsMap 提交<参数，值>Map
     * @return 提交响应
     */
    @Deprecated
    public static <T> T post(String url, Map<String, ?> paramsMap, Class<T> clazz) {
        //JSON.parseObject

        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKE_TTIME_OUT).setConnectTimeout(CONNECT_TIME_OUT).build();//设置请求和传输超时时间
            if (paramsMap != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, ?> param : paramsMap.entrySet()) {
                    if (null == param.getValue()) {
                        NameValuePair pair = new BasicNameValuePair(param.getKey(), null);
                        paramList.add(pair);
                    } else if (checkType(param.getValue())) {
                        String strs[] = (String[]) param.getValue();
                        for (String str : strs) {
                            NameValuePair pair = new BasicNameValuePair(param.getKey(), str);
                            paramList.add(pair);
                        }
                    } else {
                        NameValuePair pair = new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue()));
                        paramList.add(pair);
                    }
                }
                method.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            }
            method.setConfig(requestConfig);
            response = client.execute(method);
            //client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            logger.error(e, e);
            throw new RuntimeException(e);
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                logger.error(e, e);
                throw new RuntimeException(e);
            }
        }
        if (checkString(clazz)) {
            return (T) responseText;
        } else {
            return JacksonUtil.nonDefaultMapper().fromJson(responseText, clazz);
        }

    }

    public static <T> T get(String url, Map<String, ?> paramsMap, Class<T> clazz) {
        return get(url, paramsMap, clazz, CONNECT_TIME_OUT);
    }

    /**
     * 基于HttpClient 4.5的通用get方法 无状态返回 无Cookie
     *
     * @param url       提交的URL
     * @param paramsMap 提交<参数，值>Map
     * @return 提交响应
     */
    @Deprecated
    public static <T> T get(String url, Map<String, ?> paramsMap, Class<T> clazz, int connectTimeOut) {
        //JSON.parseObject
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpGet method = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKE_TTIME_OUT).setConnectTimeout(connectTimeOut).build();//设置请求和传输超时时间
            if (paramsMap != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, ?> param : paramsMap.entrySet()) {
                    if (null == param.getValue()) {
                        NameValuePair pair = new BasicNameValuePair(param.getKey(), null);
                        paramList.add(pair);
                    } else if (checkType(param.getValue())) {
                        String strs[] = (String[]) param.getValue();
                        for (String str : strs) {
                            NameValuePair pair = new BasicNameValuePair(param.getKey(), str);
                            paramList.add(pair);
                        }
                    } else {
                        NameValuePair pair = new BasicNameValuePair(
                                param.getKey(), String.valueOf(param.getValue()));
                        paramList.add(pair);
                    }
                }
                method.setURI(new URI(method.getURI().toString() + "?" + EntityUtils.toString(new UrlEncodedFormEntity(paramList))));
                //method.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            }
            method.setConfig(requestConfig);
            response = client.execute(method);
            //client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            logger.error(e, e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                logger.error(e, e);
                throw new RuntimeException(e);
            }
        }
        if (checkString(clazz)) {
            return (T) responseText;
        } else {
            return JacksonUtil.nonDefaultMapper().fromJson(responseText, clazz);
        }
    }

    /**
     * 基于HttpClient 4.5的通用POST方法 无状态返回 有Cookie
     *
     * @param url       提交的URL
     * @param paramsMap 提交<参数，值>Map
     * @return 提交响应
     */
    public static <T> T postCookie(String url, Map<String, ?> paramsMap, Class<T> clazz) {
        //JSON.parseObject
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKE_TTIME_OUT).setConnectTimeout(CONNECT_TIME_OUT).build();//设置请求和传输超时时间
            if (paramsMap != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, ?> param : paramsMap.entrySet()) {
                    if (null == param.getValue()) {
                        NameValuePair pair = new BasicNameValuePair(param.getKey(), null);
                        paramList.add(pair);
                    } else if (checkType(param.getValue())) {
                        String strs[] = (String[]) param.getValue();
                        for (String str : strs) {
                            NameValuePair pair = new BasicNameValuePair(param.getKey(), str);
                            paramList.add(pair);
                        }
                    } else {
                        NameValuePair pair = new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue()));
                        paramList.add(pair);
                    }
                }
                method.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            }
            method.setConfig(requestConfig);
            HttpServletRequest request = WrapWebUtils.getRequest();
            method.setHeader("Cookie", request.getHeader("Cookie"));
            if (WrapWebUtils.getRequest() != null && WrapWebUtils.getRequest().getAttribute("chengdouCookie") != null) {
                //成都集成API方式request头里面请求没有Cookie要人为早一个
                method.setHeader("Cookie", WrapWebUtils.getRequest().getAttribute("chengdouCookie").toString());
            }
            response = client.execute(method);
            //client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            logger.error(e, e);
            throw new RuntimeException(e);
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                logger.error(e, e);
                throw new RuntimeException(e);
            }
        }
        if (checkString(clazz)) {
            return (T) responseText;
        } else {
            return JacksonUtil.nonDefaultMapper().fromJson(responseText, clazz);
        }
    }

    /**
     * 基于HttpClient 4.5的通用get方法 无状态返回 有Cookie
     *
     * @param url       提交的URL
     * @param paramsMap 提交<参数，值>Map
     * @return 提交响应
     */
    public static <T> T getCookie(String url, Map<String, ?> paramsMap, Class<T> clazz) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpGet method = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKE_TTIME_OUT).setConnectTimeout(CONNECT_TIME_OUT).build();//设置请求和传输超时时间
            if (paramsMap != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, ?> param : paramsMap.entrySet()) {
                    if (null == param.getValue()) {
                        NameValuePair pair = new BasicNameValuePair(param.getKey(), null);
                        paramList.add(pair);
                    } else if (checkType(param.getValue())) {
                        String strs[] = (String[]) param.getValue();
                        for (String str : strs) {
                            NameValuePair pair = new BasicNameValuePair(param.getKey(), str);
                            paramList.add(pair);
                        }
                    } else {
                        NameValuePair pair = new BasicNameValuePair(
                                param.getKey(), String.valueOf(param.getValue()));
                        paramList.add(pair);
                    }
                }
                method.setURI(new URI(method.getURI().toString() + "?" + EntityUtils.toString(new UrlEncodedFormEntity(paramList))));
                //method.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            }
            method.setConfig(requestConfig);
            HttpServletRequest request = WrapWebUtils.getRequest();
            method.setHeader("Cookie", request.getHeader("Cookie"));
            if (WrapWebUtils.getRequest() != null && WrapWebUtils.getRequest().getAttribute("chengdouCookie") != null) {
                //成都集成API方式request头里面请求没有Cookie要人为早一个
                method.setHeader("Cookie", WrapWebUtils.getRequest().getAttribute("chengdouCookie").toString());
            }
            /*for(Enumeration<String> param = request.getHeaderNames(); param.hasMoreElements();){
                String name = param.nextElement().toString();
            	method.setHeader(name, request.getHeader(name));
            }
            */
            response = client.execute(method);
            // client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            logger.error(e, e);
            throw new RuntimeException(e);
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                logger.error(e, e);
                throw new RuntimeException(e);
            }
        }
        if (checkString(clazz)) {
            return (T) responseText;
        } else {
            return JacksonUtil.nonDefaultMapper().fromJson(responseText, clazz);
        }
    }

    /**
     * 带解密功能的getCookie
     *
     * @param url
     * @param paramsMap
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getCookieDecrypt(String url, Map<String, ?> paramsMap, Class<T> clazz) {
//        Map<String, Object> resultMap = getCookie(url, paramsMap, Map.class);
//        String key = String.valueOf(resultMap.get("key"));
//        String data = String.valueOf(resultMap.get("data"));
//        String json;
//        try {
//            json = DesPassUtil.decrypt(data, DesPassUtil.PRIVATE_KEY + key);
//        } catch (Exception e) {
//            logger.error(e,e);
//            throw new RuntimeException(e);
//        }
        return getCookie(url, paramsMap, clazz);
    }

    /**
     * 带加密功能的postCookie
     *
     * @param url
     * @param paramsMap
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T postCookieEncrypt(String url, Map<String, ?> paramsMap, Class<T> clazz) {
//        Map<String, Object> params = Maps.newHashMap();
//        String key = String.valueOf(new DateTime().getMillis());
//        params.put("key", key);
//        String data = JacksonUtil.nonDefaultMapper().toJson(paramsMap);
//        try {
//            params.put("data", DesPassUtil.encrypt(data, DesPassUtil.PRIVATE_KEY + key));
//        } catch (Exception e) {
//            logger.error(e,e);
//            throw new RuntimeException(e);
//        }
        return postCookie(url, paramsMap, clazz);
    }

    /**
     * 带解密功能的get
     *
     * @param url
     * @param paramsMap
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getDecypt(String url, Map<String, ?> paramsMap, Class<T> clazz) {
        Map<String, Object> resultMap = get(url, paramsMap, Map.class);
        String key = String.valueOf(resultMap.get("key"));
        String data = String.valueOf(resultMap.get("data"));
        String json;
        try {
            json = DesPassUtil.decrypt(data, DesPassUtil.PRIVATE_KEY + key);
        } catch (Exception e) {
            logger.error(e, e);
            throw new RuntimeException(e);
        }
        return JacksonUtil.nonDefaultMapper().fromJson(json, clazz);
    }

    /**
     * 带加密功能的post
     *
     * @param url
     * @param paramsMap
     * @param clazz
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T postEncypt(String url, Map<String, ?> paramsMap, Class<T> clazz, String key) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("key", key);
        String data = JacksonUtil.nonDefaultMapper().toJson(paramsMap);
        try {
            params.put("data", DesPassUtil.encrypt(data, DesPassUtil.PRIVATE_KEY + key));
        } catch (Exception e) {
            logger.error(e, e);
            throw new RuntimeException(e);
        }
        return post(url, params, clazz);
    }


    /**
     * 基于HttpClient 4.5的通用POST方法 无状态返回 有Cookie
     *
     * @param url       提交的URL
     * @param paramsMap 提交<参数，值>Map
     * @return 提交响应
     */
    public static <T> Map<String, Object> postCookieAndResult(String url, Map<String, ?> paramsMap, Class<T> clazz) {
        //JSON.parseObject
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        Map<String, Object> map = Maps.newHashMap();
        try {
            HttpPost method = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKE_TTIME_OUT).setConnectTimeout(CONNECT_TIME_OUT).build();//设置请求和传输超时时间
            if (paramsMap != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, ?> param : paramsMap.entrySet()) {
                    if (null == param.getValue()) {
                        NameValuePair pair = new BasicNameValuePair(param.getKey(), null);
                        paramList.add(pair);
                    } else if (checkType(param.getValue())) {
                        String strs[] = (String[]) param.getValue();
                        for (String str : strs) {
                            NameValuePair pair = new BasicNameValuePair(param.getKey(), str);
                            paramList.add(pair);
                        }
                    } else {
                        NameValuePair pair = new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue()));
                        paramList.add(pair);
                    }
                }
                method.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            }
            method.setConfig(requestConfig);
            HttpServletRequest request = WrapWebUtils.getRequest();
            method.setHeader("Cookie", request.getHeader("Cookie"));
            /*for(Enumeration<String> param = request.getHeaderNames(); param.hasMoreElements();){
            	String name = param.nextElement().toString();
            	method.setHeader(name, request.getHeader(name));
            }*/
            response = client.execute(method);
            int responseStatus = response.getStatusLine().getStatusCode();
            if (responseStatus != 200) {
                logger.error("HttpClient request fail:" + responseStatus);
                map.put("code", Integer.valueOf(0));
                map.put("message", "接口请求异常：" + responseStatus);
            } else {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    responseText = EntityUtils.toString(entity);
                }
                T data = JacksonUtil.nonDefaultMapper().fromJson(responseText, clazz);
                map.put("data", data);
                map.put("code", Integer.valueOf(1));
            }
        } catch (Exception e) {
            logger.error(e, e);
            throw new RuntimeException(e);
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                logger.error(e, e);
                throw new RuntimeException(e);
            }
        }
        return map;
    }

    /**
     * 基于HttpClient 4.5的通用get方法 无状态返回 有Cookie
     *
     * @param url       提交的URL
     * @param paramsMap 提交<参数，值>Map
     * @return 提交响应
     */
    public static <T> Map<String, Object> getCookieAndResult(String url, Map<String, ?> paramsMap, Class<T> clazz) {
        //JSON.parseObject
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        Map<String, Object> map = Maps.newHashMap();
        try {
            HttpGet method = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKE_TTIME_OUT).setConnectTimeout(CONNECT_TIME_OUT).build();//设置请求和传输超时时间
            if (paramsMap != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, ?> param : paramsMap.entrySet()) {
                    if (null == param.getValue()) {
                        NameValuePair pair = new BasicNameValuePair(param.getKey(), null);
                        paramList.add(pair);
                    } else if (checkType(param.getValue())) {
                        String strs[] = (String[]) param.getValue();
                        for (String str : strs) {
                            NameValuePair pair = new BasicNameValuePair(param.getKey(), str);
                            paramList.add(pair);
                        }
                    } else {
                        NameValuePair pair = new BasicNameValuePair(
                                param.getKey(), String.valueOf(param.getValue()));
                        paramList.add(pair);
                    }
                }
                method.setURI(new URI(method.getURI().toString() + "?" + EntityUtils.toString(new UrlEncodedFormEntity(paramList))));
                //method.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            }
            method.setConfig(requestConfig);
            HttpServletRequest request = WrapWebUtils.getRequest();
            method.setHeader("Cookie", request.getHeader("Cookie"));
            /*for(Enumeration<String> param = request.getHeaderNames(); param.hasMoreElements();){
            	String name = param.nextElement().toString();
            	method.setHeader(name, request.getHeader(name));
            }*/
            response = client.execute(method);
            //client.execute(method);
            int responseStatus = response.getStatusLine().getStatusCode();
            if (responseStatus != 200) {
                logger.error("HttpClient request fail:" + responseStatus);
                map.put("code", Integer.valueOf(0));
                map.put("message", "接口请求异常：" + responseStatus);
            } else {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    responseText = EntityUtils.toString(entity);
                }
                T data = JacksonUtil.nonDefaultMapper().fromJson(responseText, clazz);
                map.put("data", data);
                map.put("code", Integer.valueOf(1));
            }
        } catch (Exception e) {
            logger.error(e, e);
            throw new RuntimeException(e);
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                logger.error(e, e);
                throw new RuntimeException(e);
            }
        }
        return map;
    }

    /**
     * 基于HttpClient 4.4的通用POST json数据
     *
     * @param url        提交的URL
     * @param paramsJson 参数
     * @return 提交响应
     */
    public static <T> Map<String, Object> postJsonCookieAndResult(String url, String paramsJson, Class<T> clazz) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        Map<String, Object> map = Maps.newHashMap();
        try {
            HttpPost method = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKE_TTIME_OUT).setConnectTimeout(CONNECT_TIME_OUT).build();//设置请求和传输超时时间
            if (paramsJson != null) {
                StringEntity entity = new StringEntity(paramsJson, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                method.setEntity(entity);
            }
            method.setConfig(requestConfig);
            HttpServletRequest request = WrapWebUtils.getRequest();
            method.setHeader("Cookie", request.getHeader("Cookie"));
            /*for(Enumeration<String> param = request.getHeaderNames(); param.hasMoreElements();){
            	String name = param.nextElement().toString();
            	method.setHeader(name, request.getHeader(name));
            }*/
            response = client.execute(method);
            int responseStatus = response.getStatusLine().getStatusCode();
            if (responseStatus != 200) {
                logger.error("HttpClient request fail:" + responseStatus);
                map.put("code", Integer.valueOf(0));
                map.put("message", "接口请求异常：" + responseStatus);
            } else {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    responseText = EntityUtils.toString(entity);
                }
                T data = JacksonUtil.nonDefaultMapper().fromJson(responseText, clazz);
                map.put("data", data);
                map.put("code", Integer.valueOf(1));
            }
        } catch (Exception e) {
            logger.error(e, e);
            throw new RuntimeException(e);
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                logger.error(e, e);
                throw new RuntimeException(e);
            }
        }
        return map;
    }


    /**
     * 基于HttpClient 4.4的通用POST json数据
     *
     * @param url        提交的URL
     * @param paramsJson 参数
     * @return 提交响应
     */
    public static <T> T postJsonCookie(String url, String paramsJson, Class<T> clazz) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKE_TTIME_OUT).setConnectTimeout(CONNECT_TIME_OUT).build();//设置请求和传输超时时间
            if (paramsJson != null) {
                StringEntity entity = new StringEntity(paramsJson, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                method.setEntity(entity);
            }
            method.setConfig(requestConfig);
            HttpServletRequest request = WrapWebUtils.getRequest();
            method.setHeader("Cookie", request.getHeader("Cookie"));
            /*for(Enumeration<String> param = request.getHeaderNames(); param.hasMoreElements();){
            	String name = param.nextElement().toString();
            	method.setHeader(name, request.getHeader(name));
            }*/
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            logger.error(e, e);
            throw new RuntimeException(e);
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                logger.error(e, e);
                throw new RuntimeException(e);
            }
        }
        if (checkString(clazz)) {
            return (T) responseText;
        } else {
            return JacksonUtil.nonDefaultMapper().fromJson(responseText, clazz);
        }
    }

    /**
     * 判断是否是数组
     *
     * @param type
     * @return
     */
    public static <T> boolean checkType(T type) {
        return type.getClass().isArray();
    }

    public static <T> boolean checkString(Class<T> clazz) {
        return clazz.equals(String.class);
    }
}
