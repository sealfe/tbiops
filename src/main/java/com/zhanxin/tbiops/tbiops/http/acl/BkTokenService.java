package com.zhanxin.tbiops.tbiops.http.acl;

import com.zhanxin.tbiops.tbiops.repository.TokenCookie;
import kong.unirest.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author by fengww
 * @Classname BkTokenService
 * @Description
 * @Date 2023/7/19 22:18
 */
@Service
public class BkTokenService {


    public String getToken(String username, String password) {
        Unirest.config().verifySsl(false);
        Map<String, String> cookieMap = getLoginCookieMap();
        List<Cookie> collect = cookieMap.entrySet().stream().map(n -> new Cookie(n.getKey(), n.getValue())).collect(Collectors.toList());
        String bkloginCsrftoken = cookieMap.get("bklogin_csrftoken");
        MultipartBody body = Unirest.post("https://bkce7.tobizit.com/login/?c_url=/")
                .field("csrfmiddlewaretoken", bkloginCsrftoken)
                .field("username", username)
                .field("password", password).cookie(collect);
        HttpResponse<Object> object = body.asObject(Object.class);
        Cookies cookies = object.getCookies();
        Map<String, String> newCookieMap = cookies.stream().filter(n -> StringUtils.isNotBlank(n.getValue())).collect(Collectors.toMap(n -> n.getName(), n -> n.getValue()));
        String token = UUID.randomUUID().toString();
        cookieMap.putAll(newCookieMap);
        TokenCookie.addCookie(token, cookieMap);
        return token;
    }


    public Map<String, String> getLoginCookieMap() {
        HttpResponse<Object> object = Unirest.get("https://bkce7.tobizit.com/login/?c_url=/").asObject(Object.class);
        Cookies cookies = object.getCookies();
        return cookies.stream().filter(n -> StringUtils.isNotBlank(n.getValue())).collect(Collectors.toMap(n -> n.getName(), n -> n.getValue()));
    }


    public Map<String, String> getCookieMap(String token) {
        Map<String, String> cookieMap = TokenCookie.getCookieMap(token);
        List<Cookie> cookieList = cookieMap.entrySet().stream().map(n -> new Cookie(n.getKey(), n.getValue())).collect(Collectors.toList());
        String cookieStr = cookieMap.entrySet().stream().map(n -> n.getKey() + "=" + n.getValue()).collect(Collectors.joining(";"));
        GetRequest cookie = Unirest.get("http://bkce7.tobizit.com/console/").header("Cookie", cookieStr);
        HttpResponse<Object> object = cookie.asObject(Object.class);

        Cookies cookies = object.getCookies();
        cookies.stream().forEach(n -> {
            cookieMap.put(n.getName(), n.getValue());
        });
        TokenCookie.addCookie(token, cookieMap);
        return cookieMap;
    }


    public Object requestUrl(String s, String token) {
        Map<String, String> cookieMap1 = getCookieMap(token);
        String cookieStr = cookieMap1.entrySet().stream().map(n -> n.getKey() + "=" + n.getValue()).collect(Collectors.joining(";"));
        HttpRequestWithBody post = Unirest.post("https://bkce7.tobizit.com/" + s).header("Cookie", cookieStr);
        HttpResponse<Object> object = post.asObject(Object.class);
        Cookies cookies = object.getCookies();
        cookies.stream().forEach(n -> {
            cookieMap1.put(n.getName(), n.getValue());
        });
        TokenCookie.addCookie(token, cookieMap1);
        return object.getBody();
    }
}
