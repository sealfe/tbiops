package com.zhanxin.tbiops.tbiops.http.acl;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class UniresetRequest {
    private final HttpServletRequest httpRequest;
    private final JsonObject jsonObject;
    private final Map<String, String> cookieMap;
    private final String token;

    private Boolean open;

    private List<String> ignoreHeaders = Lists.newArrayList("postman-token","content-length","host","connection","cache-control","user-agent","origin","referer"     );


    public UniresetRequest(HttpServletRequest httpRequest, JsonObject jsonObject, Map<String, String> cookieMap, String token, Boolean open) {
        this.httpRequest = httpRequest;
        this.jsonObject = jsonObject;
        this.cookieMap = cookieMap;
        this.token = token;
        this.open = open;
    }

    public HttpServletRequest getHttpRequest() {
        return httpRequest;
    }

    public JsonObject getJsonObject() {
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
        if(open){
            requestURI = requestURI.replace("bkapi/open/api/", "");
        }else{
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
}
