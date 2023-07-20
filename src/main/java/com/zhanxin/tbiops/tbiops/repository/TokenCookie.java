package com.zhanxin.tbiops.tbiops.repository;

import com.google.common.collect.Maps;
import com.zhanxin.tbiops.tbiops.dto.JsonException;
import lombok.Data;

import java.util.Map;

/**
 * @author by fengww
 * @Classname TokenCookie
 * @Description
 * @Date 2023/7/19 22:15
 */
@Data
public class TokenCookie {


    private static Map<String, Map<String, String>> cookieMap = Maps.newHashMap();


    public static void addCookie(String key, Map<String, String> newCookieMap) {
        Map<String, String> cookieMapOrDefault = cookieMap.getOrDefault(key, Maps.newHashMap());
        cookieMapOrDefault.putAll(newCookieMap);
        cookieMap.put(key, cookieMapOrDefault);
    }


    public static Map<String, String> getCookieMap(String key) {
        return cookieMap.getOrDefault(key, Maps.newHashMap());
    }


    public static void checkCookie(String token) {
        if(!cookieMap.containsKey(token)){
            throw new JsonException("1023","请先登录");
        }
    }
}
