package com.zhanxin.tbiops.common;

import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;

import java.util.Map;

class MapUtilsTest {

    //test a value is null in map
    @Test
    void getRailedValueMap() {
        MapUtils.getRailedValueMap(null);
    }

    //test a value is not null in map
    @Test
    void getRailedValueMap1() {
        Map<String, String> key = Map.of("key", "value");
        MapUtils.getRailedValueMap(key);
        assert key.get("key").equals("value");
    }

    //test a value is null in map and the map is not null
    @Test
    void getRailedValueMap2() {
        Map<String, Object> key = Maps.newHashMap();
        key.put("key", null);
        key = MapUtils.getRailedValueMap(key);
        assert !key.containsKey("key");
    }

    //test a value is not null in map and the map is not null and the value is empty
    @Test
    void getRailedValueMap3() {
        Map<String, Object> key = Maps.newHashMap();
        key.put("key", "");
        key = MapUtils.getRailedValueMap(key);
        assert !key.containsKey("key");
    }


}