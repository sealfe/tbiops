package com.zhanxin.tbiops.tbiops.repository;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class RedisService {


    @Autowired
    private RedisTemplate redisTemplate;


    public void addCookie(String key, Map<String, String> newCookieMap) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        Map<String, String> entries = Optional.ofNullable(hashOperations.entries(key)).orElse(Maps.newHashMap());
        newCookieMap.entrySet().stream().filter(n -> n.getValue() != null && StringUtils.isNotBlank(n.getValue().toString())).forEach(n -> entries.put(n.getKey(), n.getValue()));
        hashOperations.putAll(key, entries);
    }


    public Map<String, String> getCookieMap(String key) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        return Optional.ofNullable(hashOperations.entries(key)).orElse(Maps.newHashMap());
    }

    public Boolean cookieValid(String token) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        Set keys = hashOperations.keys(token);
        return keys.size() > 0;
    }


    public void addCookie(String token, Map<String, String> cookieMap, long second) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.putAll(token, cookieMap);
        redisTemplate.expire(token, second, java.util.concurrent.TimeUnit.SECONDS);
    }
}
