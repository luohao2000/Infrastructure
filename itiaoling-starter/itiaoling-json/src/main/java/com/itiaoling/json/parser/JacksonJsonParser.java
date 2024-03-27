package com.itiaoling.json.parser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.itiaoling.json.parser.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JacksonJsonParser implements JsonParser {

    private ObjectMapper objectMapper;

    public JacksonJsonParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> String toJson(T obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return objectMapper.readValue(json, classOfT);
        } catch (IOException e) {
           e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> List<T> fromJsonToList(String jsonList, Class<T> clsOfElement) {
        CollectionType javaType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, clsOfElement);
        try {
            return objectMapper.readValue(jsonList, javaType);
        } catch (IOException e) {
           e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> Map<String, T> fromJsonToMap(String jsonMap, Class<T> clsOfElement) {
        MapType javaType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class,
                clsOfElement);
        try {
            return objectMapper.readValue(jsonMap, javaType);
        } catch (IOException e) {
          e.printStackTrace();
        }
        return null;
    }


    //////////////////////////////////////////////////////////////////////////////
    //  region 兼容逻辑
    public static JacksonJsonParser build() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许字段名不带引号
        mapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许属性为空的beans创建
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return new JacksonJsonParser(mapper);
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().equals("");
    }

    // json 转 java 对象
    public <T> T fromJson(String jsonString, TypeReference<T> valueType) {
        if (isEmpty(jsonString)) {
            return null;
        } else {
            try {
                return objectMapper.readValue(jsonString, valueType);
            } catch (IOException var4) {
                return null;
            }
        }
    }
    // endregion
}
