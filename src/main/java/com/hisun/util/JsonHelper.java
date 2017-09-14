package com.hisun.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 此类是处理json字符串的工具类
 */
public class JsonHelper {

    public static Map<String, Object> jsonStr2Map(String jsonString) {
        return new Gson().fromJson(jsonString, new TypeToken<HashMap<String, Object>>() {
        }.getType());

    }

    /**
     * 将json数组转换为List集合
     *
     * @param jsonArr
     * @return
     * @throws JSONException
     */
    private static List<?> jsonToList(JSONArray jsonArr) throws JSONException {
        List<Object> jsonToMapList = new ArrayList<Object>();
        for (int i = 0; i < jsonArr.length(); i++) {
            Object object = jsonArr.get(i);
            if (object instanceof JSONArray) {
                jsonToMapList.add(JsonHelper.jsonToList((JSONArray) object));
            } else if (object instanceof JSONObject) {
                jsonToMapList.add(JsonHelper.jsonToMap((JSONObject) object));
            } else {
                jsonToMapList.add(object);
            }
        }
        return jsonToMapList;
    }

    /**
     * 将json对象转换为Map集合
     *
     * @param jsonObj
     * @return
     * @throws JSONException
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> jsonToMap(JSONObject jsonObj)
            throws JSONException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Iterator<String> jsonKeys = jsonObj.keys();
        while (jsonKeys.hasNext()) {
            String jsonKey = jsonKeys.next();
            Object jsonValObj = jsonObj.get(jsonKey);
            if (jsonValObj instanceof JSONArray) {
                jsonMap.put(jsonKey,
                        JsonHelper.jsonToList((JSONArray) jsonValObj));
            } else if (jsonValObj instanceof JSONObject) {
                jsonMap.put(jsonKey,
                        JsonHelper.jsonToMap((JSONObject) jsonValObj));
            } else {
                jsonMap.put(jsonKey, jsonValObj);
            }
        }
        return jsonMap;
    }

    /**
     * json字符串的格式化
     *
     * @param json                           需要去除格式的json串
     * @param 去除fillStringUnit每一层之前的占位符号比如空格 、制表符、换行等
     * @return
     * @author wangx
     */
    public static String deformatJson(String json) {
        String dest = "";
        if (json != null) {
            Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
            Matcher matcher = pattern.matcher(json);
            dest = matcher.replaceAll("");
        }
        return dest;
        // return json.replace("\n", "").replace("\r", "");
    }

    /**
     * json字符串的格式化
     *
     * @param json                         需要格式的json串
     * @param fillStringUnit每一层之前的占位符号比如空格 制表符
     * @return
     * @author wangx
     */
    public static String formatJson(String json, String fillStringUnit) {
        if (json == null || json.trim().length() == 0) {
            return null;
        }

        int fixedLenth = 0;
        ArrayList<String> tokenList = new ArrayList<String>();
        {
            String jsonTemp = json;
            // 预读取
            while (jsonTemp.length() > 0) {
                String token = getToken(jsonTemp);
                jsonTemp = jsonTemp.substring(token.length());
                token = token.trim();
                tokenList.add(token);
            }
        }

        for (int i = 0; i < tokenList.size(); i++) {
            String token = tokenList.get(i);
            int length = token.getBytes().length;
            if (length > fixedLenth && i < tokenList.size() - 1
                    && tokenList.get(i + 1).equals(":")) {
                fixedLenth = length;
            }
        }

        StringBuilder buf = new StringBuilder();
        int count = 0;
        for (int i = 0; i < tokenList.size(); i++) {

            String token = tokenList.get(i);

            if (token.equals(",")) {
                buf.append(token);
                doFill(buf, count, fillStringUnit);
                continue;
            }
            if (token.equals(":")) {
                buf.append(" ").append(token).append(" ");
                continue;
            }
            if (token.equals("{")) {
                String nextToken = tokenList.get(i + 1);
                if (nextToken.equals("}")) {
                    i++;
                    buf.append("{ }");
                } else {
                    count++;
                    buf.append(token);
                    doFill(buf, count, fillStringUnit);
                }
                continue;
            }
            if (token.equals("}")) {
                count--;
                doFill(buf, count, fillStringUnit);
                buf.append(token);
                continue;
            }
            if (token.equals("[")) {
                String nextToken = tokenList.get(i + 1);
                if (nextToken.equals("]")) {
                    i++;
                    buf.append("[ ]");
                } else {
                    count++;
                    buf.append(token);
                    doFill(buf, count, fillStringUnit);
                }
                continue;
            }
            if (token.equals("]")) {
                count--;
                doFill(buf, count, fillStringUnit);
                buf.append(token);
                continue;
            }

            buf.append(token);
            // 左对齐
            if (i < tokenList.size() - 1 && tokenList.get(i + 1).equals(":")) {
                int fillLength = fixedLenth - token.getBytes().length;
                if (fillLength > 0) {
                    for (int j = 0; j < fillLength; j++) {
                        buf.append(" ");
                    }
                }
            }
        }
        return buf.toString();
    }

    private static String getToken(String json) {
        StringBuilder buf = new StringBuilder();
        boolean isInYinHao = false;
        while (json.length() > 0) {
            String token = json.substring(0, 1);
            json = json.substring(1);

            if (!isInYinHao
                    && (token.equals(":") || token.equals("{")
                    || token.equals("}") || token.equals("[")
                    || token.equals("]") || token.equals(","))) {
                if (buf.toString().trim().length() == 0) {
                    buf.append(token);
                }

                break;
            }

            if (token.equals("\\")) {
                buf.append(token);
                buf.append(json.substring(0, 1));
                json = json.substring(1);
                continue;
            }
            if (token.equals("\"")) {
                buf.append(token);
                if (isInYinHao) {
                    break;
                } else {
                    isInYinHao = true;
                    continue;
                }
            }
            buf.append(token);
        }
        return buf.toString();
    }

    private static void doFill(StringBuilder buf, int count,
                               String fillStringUnit) {
        buf.append("\n");
        for (int i = 0; i < count; i++) {
            buf.append(fillStringUnit);
        }
    }

    /**
     * json字符串的格式化，uglyJsonString2prettyJsonString
     *
     * @param json 需要格式的json串
     * @return
     * @author wangx
     */
    public static String jsonFormatter(String uglyJSONString) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(uglyJSONString);
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }

}