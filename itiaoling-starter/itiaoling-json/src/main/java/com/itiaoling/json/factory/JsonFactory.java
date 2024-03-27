package com.itiaoling.json.factory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.itiaoling.json.parser.JacksonJsonParser;
import com.itiaoling.json.parser.JsonParser;


public class JsonFactory {

    /**
     * 创建  ObjectMapper ，并增加自定义功能，比如 自定义注解
     *
     * @param withConfiguration 是否包含配置
     * @return
     */
    public static ObjectMapper newJacksonObjectMapper(boolean withConfiguration) {
        ObjectMapper objectMapper = new ObjectMapper();

        // 所有的自定义注解拦截器
//        objectMapper.setAnnotationIntrospector(new CommonJacksonAnnotationIntrospector());

        if (withConfiguration) {
            // TODO 设置默认配置
            objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // 允许字段名不带引号
            objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            // 允许属性为空的beans创建
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            // enable JSR310Module
            objectMapper.registerModule(new JavaTimeModule());
        }

        return objectMapper;
    }




    public static JsonParser newJsonParser() {
        return new JacksonJsonParser(JsonFactory.newJacksonObjectMapper(true));
    }


    public static JsonParser newJsonParserWithCaseInsensitive() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, false);
        return new JacksonJsonParser(objectMapper);
    }

    public static JsonParser newJsonParser(ObjectMapper objectMapper) {
        return new JacksonJsonParser(objectMapper);
    }
}
