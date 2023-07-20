package com.zhanxin.tbiops.tbiops.config;

import com.google.common.collect.Maps;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author by fengww
 * @Classname UrlConfig
 * @Description
 * @Date 2023/7/21 02:26
 */
@Component
@Getter
public class UrlConfig {


    @Value("${bk.base.cmdb_url}")
    public static String cmdbUrl;


    public static Map<String, String> moduleMap = Maps.newHashMap();


    public UrlConfig(@Value("${bk.base.cmdb_url}") String cmdbUrl) {
        UrlConfig.cmdbUrl = cmdbUrl;
        moduleMap.put("cmdb", cmdbUrl);
    }


}
