package com.itiaoling.json.parser;

import java.util.List;
import java.util.Map;

public interface JsonParser {

    /**
     * Java 对象转换成json
     *
     * @param obj
     * @param <T>
     * @return
     */
    <T> String toJson(T obj);

    /**
     * json 转换成 Java 对象
     *
     * @param json
     * @param classOfT
     * @param <T>
     * @return
     */
    <T> T fromJson(String json, Class<T> classOfT);

    /**
     * json 转换成 Java List
     *
     * @param jsonList
     * @param clsOfElement List Value的类型
     * @param <T>
     * @return
     */
    <T> List<T> fromJsonToList(String jsonList, Class<T> clsOfElement);

    /**
     * json 转换成 Java Map
     *
     * @param jsonMap      json 字符串
     * @param clsOfElement Map Value的类型
     * @param <T>
     * @return
     */
    <T> Map<String, T> fromJsonToMap(String jsonMap, Class<T> clsOfElement);
}
