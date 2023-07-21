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




    public static Map<String, String> moduleMap = Maps.newHashMap();


    public UrlConfig(@Value("${bk.base.cmdb_url}") String cmdbUrl, @Value("${bk.base.cmdb_inner_url}") String cmdbInnerUrl) {
        moduleMap.put("cmdb", cmdbUrl);
        moduleMap.put("cmdb_inner", cmdbInnerUrl);
    }


}
