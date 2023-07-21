package com.zhanxin.tbiops.http.acl;

import com.google.common.collect.Lists;
import com.zhanxin.tbiops.config.UrlConfig;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class UniresetRequest {

    private final HttpServletRequest httpRequest;

    private final Map jsonObject;

    private final Map<String, String> cookieMap;

    private final String token;

    private final String module;

    private Boolean open;

    private List<String> ignoreHeaders = Lists.newArrayList("postman-token", "content-length", "host", "cache-control", "user-agent", "origin", "referer");


    public UniresetRequest(HttpServletRequest httpRequest, Map jsonObject, Map<String, String> cookieMap, String token, Boolean open) {
        this.httpRequest = httpRequest;
        this.jsonObject = jsonObject;
        this.cookieMap = cookieMap;
        this.token = token;
        this.open = open;
        this.module = httpRequest.getParameter("module");
    }

    public HttpServletRequest getHttpRequest() {
        return httpRequest;
    }

    public Map getJsonObject() {
        return jsonObject;
    }

    public Map<String, String> getCookieMap() {
        return cookieMap;
    }

    public String getToken() {
        return token;
    }

    public String getRealUrl() {
        String requestURI = httpRequest.getRequestURI();
        if (open) {
            requestURI = requestURI.replace("bkapi/open/api/", "");
        } else {
            requestURI = requestURI.replace("bkapi/private/api/", "");
        }
        return requestURI;
    }


    public List<String> getHeaderNames() {
        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        List<String> names = Lists.newArrayList();
        headerNames.asIterator().forEachRemaining(n -> {
            if (!ignoreHeaders.contains(n)) {
                names.add(n);
            }
        });
        return names;
    }


    public Boolean getOpen() {
        return open;
    }


    public String baseUrl() {
        return UrlConfig.moduleMap.get(module);
    }
}
