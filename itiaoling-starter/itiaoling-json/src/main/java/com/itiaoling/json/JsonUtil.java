package com.itiaoling.json;




import com.itiaoling.json.factory.JsonFactory;
import com.itiaoling.json.parser.JsonParser;


import java.util.List;
import java.util.Map;


public class JsonUtil {

    private static JsonParser jsonParser;

    static {
        // 尝试从 Spring Bean中获取
        try {
            jsonParser = JsonFactory.newJsonParser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isEmpty(String s) {
        return s == null || s.trim().equals("");
    }

    /**
     * Java 对象转换成json，包含 null 字段
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String toJson(T obj) {
        try {
            return jsonParser.toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * json string 转换成 Java 对象，不包含 List、Map <br/>
     * List、Map 使用 fromJsonToList 或者 fromJsonToMap 方法进行转换 <br/>
     * 例如：<br/>
     * List 可以使用 fromJsonToList(jsonPersonList, Person.class) <br/>
     * Map 可以使用 fromJsonToMap(jsonPersonMap, Person.class) <br/>
     *
     * @param json
     * @param classOfT
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {

        if (isEmpty(json)) {
            return null;
        } else {
            try {
                return jsonParser.fromJson(json, classOfT);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * json 转换成 Java List
     *
     * @param jsonList
     * @param clsOfElement List Value的类型
     * @param <T>
     * @return
     */
    public static <T> List<T> fromJsonToList(String jsonList, Class<T> clsOfElement) {
        if (isEmpty(jsonList)) {
            return null;
        } else {
            try {
                return jsonParser.fromJsonToList(jsonList, clsOfElement);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

    }

    /**
     * json 转换成 Java Map
     *
     * @param jsonMap      json 字符串
     * @param clsOfElement Map Value的类型
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> fromJsonToMap(String jsonMap, Class<T> clsOfElement) {

        if (isEmpty(jsonMap)) {
            return null;
        } else {
            try {
                return jsonParser.fromJsonToMap(jsonMap, clsOfElement);
            } catch (Exception e) {
               e.printStackTrace();
                return null;
            }
        }
    }
}