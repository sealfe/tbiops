package com.zhanxin.tbiops.tbiops.http.acl;

import com.zhanxin.tbiops.tbiops.dto.JsonException;
import com.zhanxin.tbiops.tbiops.repository.TokenCookie;
import kong.unirest.*;
import kong.unirest.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.zhanxin.tbiops.tbiops.common.JsonUtils.toObject;

/**
 * @author by fengww
 * @Classname BkTokenService
 * @Description
 * @Date 2023/7/19 22:18
 */
@Service
public class BkTokenService {

    @Value("${bk.base.url}")
    private String baseUrl;


    public String getToken(String username, String password) {
        Unirest.config().verifySsl(false);
        Map<String, String> cookieMap = getLoginCookieMap();
        List<Cookie> collect = cookieMap.entrySet().stream().map(n -> new Cookie(n.getKey(), n.getValue())).collect(Collectors.toList());
        String bkloginCsrftoken = cookieMap.get("bklogin_csrftoken");
        MultipartBody body = Unirest.post(baseUrl + "/login/?c_url=/")
                .field("csrfmiddlewaretoken", bkloginCsrftoken)
                .field("username", username)
                .field("password", password).cookie(collect);
        HttpResponse<JsonNode> object = body.asJson();
        JSONObject jsonObject = object.getBody().getObject();
        Map resultMap = toObject(jsonObject.toString(), Map.class);
        if (Objects.equals(resultMap.get("result"), false)) {
            throw new JsonException(resultMap.getOrDefault("code", "1023").toString(), resultMap.getOrDefault("message", "账户或者密码错误，请重新输入").toString());
        }
        Cookies cookies = object.getCookies();
        Map<String, String> newCookieMap = cookies.stream().filter(n -> StringUtils.isNotBlank(n.getValue())).collect(Collectors.toMap(n -> n.getName(), n -> n.getValue()));
        String token = UUID.randomUUID().toString();
        cookieMap.putAll(newCookieMap);
        TokenCookie.addCookie(token, cookieMap);
        return token;
    }


    public Map<String, String> getLoginCookieMap() {
        HttpResponse<Object> object = Unirest.get(baseUrl + "/login/?c_url=/").asObject(Object.class);
        Cookies cookies = object.getCookies();
        return cookies.stream().filter(n -> StringUtils.isNotBlank(n.getValue())).collect(Collectors.toMap(n -> n.getName(), n -> n.getValue()));
    }


    public Map<String, String> getCookieMap(String token) {
        Map<String, String> cookieMap = TokenCookie.getCookieMap(token);
        String cookieStr = cookieMap.entrySet().stream().map(n -> n.getKey() + "=" + n.getValue()).collect(Collectors.joining(";"));
        GetRequest cookie = Unirest.get(baseUrl + "/console/").header("Cookie", cookieStr).header("X-Csrftoken", cookieMap.get("bk_csrftoken"));
        HttpResponse<Object> object = cookie.asObject(Object.class);

        Cookies cookies = object.getCookies();
        cookies.stream().forEach(n -> {
            cookieMap.put(n.getName(), n.getValue());
        });
        TokenCookie.addCookie(token, cookieMap);
        return cookieMap;
    }


     public Map<String, String> getPrivateCookieMap(String token) {
        Map<String, String> cookieMap = TokenCookie.getCookieMap(token);
        String cookieStr = cookieMap.entrySet().stream().map(n -> n.getKey() + "=" + n.getValue()).collect(Collectors.joining(";"));
        GetRequest cookie = Unirest.get("http://cmdb.bkce7.tobizit.com/#/index").header("Cookie", cookieStr).header("X-Csrftoken", cookieMap.get("bk_csrftoken"));
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
        HttpRequestWithBody post = Unirest.post(baseUrl + s).header("Cookie", cookieStr).header("X-Csrftoken", cookieMap1.get("bk_csrftoken"));
        HttpResponse<Object> object = post.asObject(Object.class);
        Cookies cookies = object.getCookies();
        cookies.stream().forEach(n -> {
            cookieMap1.put(n.getName(), n.getValue());
        });
        TokenCookie.addCookie(token, cookieMap1);
        return object.getBody();
    }

    public String getBaseUrl() {
        return baseUrl;
    }


    public void updateCookieMap(String token, Cookies cookies) {
        Map<String, String> cookieMap = TokenCookie.getCookieMap(token);
        cookies.forEach(n -> {
            cookieMap.put(n.getName(), n.getValue());
        });
        TokenCookie.addCookie(token, cookieMap);
    }


    public String getBkToken(String token) {
        return getCookieMap(token).get("bk_token");
    }

    public String getCrsfToken(String token) {
        return getCookieMap(token).get("bk_csrftoken");
    }

}
