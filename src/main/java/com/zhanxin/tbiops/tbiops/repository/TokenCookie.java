package com.zhanxin.tbiops.tbiops.repository;

import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
        newCookieMap.entrySet().stream().filter(n -> n.getValue() != null && StringUtils.isNotBlank(n.getValue().toString())).forEach(n -> cookieMapOrDefault.put(n.getKey(), n.getValue()));
        cookieMap.put(key, cookieMapOrDefault);
    }


    public static Map<String, String> getCookieMap(String key) {
        return cookieMap.getOrDefault(key, Maps.newHashMap());
    }


    public static Boolean cookieValid(String token) {
        return cookieMap.containsKey(token);
    }
}
