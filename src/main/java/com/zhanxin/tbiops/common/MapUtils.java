package com.zhanxin.tbiops.common;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapUtils {


    public static Map getRailedValueMap(Map map) {
        HashMap objectObjectHashMap = Maps.newHashMap();
        if (map != null) {
            Set<Map.Entry> set = map.entrySet();
            set.stream().filter(n -> n.getValue() != null && StringUtils.isNotBlank(n.getValue().toString())).forEach(n -> {
                if (n.getValue() instanceof Map) {
                    objectObjectHashMap.put(n.getKey(), getRailedValueMap((Map) n.getValue()));
                }else {
                    objectObjectHashMap.put(n.getKey(), n.getValue());
                }
            });
        }
        return objectObjectHashMap;
    }


}
