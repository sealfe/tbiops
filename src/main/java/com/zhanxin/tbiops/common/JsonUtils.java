package com.zhanxin.tbiops.common;

import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.SneakyThrows;

/**
 * @author by fengww
 * @Classname JsonUtils
 * @Description
 * @Date 2023/7/20 23:09
 */
public class JsonUtils {




    @SneakyThrows
    public static String toJsonStirng(Object object){
        return new JsonMapper().writeValueAsString(object);
    }


    @SneakyThrows
    public static <T> T toObject(String json, Class<T> clazz){
        return new JsonMapper().readValue(json,clazz);
    }



}
