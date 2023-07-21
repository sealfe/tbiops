package com.zhanxin.tbiops.config;

import kong.unirest.Unirest;
import org.springframework.stereotype.Component;

/**
 * @author by fengww
 * @Classname UnirestConfig
 * @Description
 * @Date 2023/7/20 23:23
 */
@Component
public class UnirestConfig {


    public UnirestConfig() {
        Unirest.config().verifySsl(false);
    }
}
