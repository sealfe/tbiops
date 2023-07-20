package com.zhanxin.tbiops.tbiops.http.acl;

import com.google.gson.JsonObject;
import com.zhanxin.tbiops.tbiops.dto.JsonException;
import kong.unirest.*;
import kong.unirest.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.zhanxin.tbiops.tbiops.common.JsonUtils.toObject;

@Service
public class BkRequestService {

    @Autowired
    private BkTokenService bkTokenService;


    @Value("${bk.base.app_code}")
    private String appCode;

    @Value("${bk.base.app_secret}")
    private String appSecret;

    @Value("${bk.base.open_url}")
    private String openUrl;

    @Value("${bk.base.url}")
    private String baseUrl;


    public Object openRequestUrl(HttpServletRequest httpRequest, JsonObject jsonObject) {
        String token = httpRequest.getParameter("token");
        jsonObject.addProperty("bk_app_code", appCode);
        jsonObject.addProperty("bk_app_secret", appSecret);
        Map<String, String> cookieMap = bkTokenService.getCookieMap(token);
        jsonObject.addProperty("bk_token", cookieMap.get("bk_token"));
        String method = httpRequest.getMethod();
        HttpResponse httpResponse = getObject(httpRequest, jsonObject, token, cookieMap, method, true);
        Cookies cookies = httpResponse.getCookies();
        bkTokenService.updateCookieMap(token, cookies);
        return httpResponse.getBody();
    }

    public Object privateRequestUrl(HttpServletRequest httpRequest, JsonObject jsonObject) {
        String token = httpRequest.getParameter("token");
        Map<String, String> cookieMap = bkTokenService.getCookieMap(token);
        String method = httpRequest.getMethod();
        HttpResponse<JsonNode> httpResponse = getObject(httpRequest, jsonObject, token, cookieMap, method, false);
        Cookies cookies = httpResponse.getCookies();
        bkTokenService.updateCookieMap(token, cookies);

        return httpResponse.getBody();
    }

    private HttpResponse<JsonNode> getObject(HttpServletRequest httpRequest, JsonObject jsonObject, String token, Map<String, String> cookieMap, String method, boolean open) {
        HttpResponse<JsonNode> jsonNodeHttpResponse = getJsonNodeHttpResponse(httpRequest, jsonObject, token, cookieMap, method, open);
        JSONObject object = jsonNodeHttpResponse.getBody().getObject();
        Map resultMap = toObject(object.toString(), Map.class);

        if (resultMap.get("result").equals(false)) {
            throw new JsonException(resultMap.getOrDefault("code", "").toString(), resultMap.getOrDefault("message", "").toString());
        }
        return jsonNodeHttpResponse;
    }

    private HttpResponse<JsonNode> getJsonNodeHttpResponse(HttpServletRequest httpRequest, JsonObject jsonObject, String token, Map<String, String> cookieMap, String method, boolean open) {
        switch (method) {
            case "GET":
                return get(new UniresetRequest(httpRequest, jsonObject, cookieMap, token, open));
            case "POST":
                return post(new UniresetRequest(httpRequest, jsonObject, cookieMap, token, open));
            case "PUT":
                return put(new UniresetRequest(httpRequest, jsonObject, cookieMap, token, open));
            case "DELETE":
                return delete(new UniresetRequest(httpRequest, jsonObject, cookieMap, token, open));
            default:
                return null;
        }
    }

    private HttpResponse<JsonNode> put(UniresetRequest uniresetRequest) {
        HttpServletRequest httpRequest = uniresetRequest.getHttpRequest();
        String requestURI = uniresetRequest.getRealUrl();
        JsonObject jsonObject = uniresetRequest.getJsonObject();
        Map<String, String> cookieMap = uniresetRequest.getCookieMap();
        requestURI = requestURI.replace("bkapi/open/api/", "");
        HttpRequestWithBody request = Unirest.put((uniresetRequest.getOpen() ? openUrl : baseUrl) + requestURI).header("X-Csrftoken", cookieMap.get("bk_csrftoken")).header("Cookie", getCookieStr(cookieMap));
        List<String> headerNames = uniresetRequest.getHeaderNames();
        headerNames.forEach(n -> request.header(n, httpRequest.getHeader(n)));
        request.body(jsonObject.toString());
        return request.asJson();
    }


    private HttpResponse<JsonNode> post(UniresetRequest uniresetRequest) {
        HttpServletRequest httpRequest = uniresetRequest.getHttpRequest();
        JsonObject jsonObject = uniresetRequest.getJsonObject();
        Map<String, String> cookieMap = uniresetRequest.getCookieMap();
        String requestURI = uniresetRequest.getRealUrl();
        HttpRequestWithBody request = Unirest.post(((uniresetRequest.getOpen() ? openUrl : baseUrl)) + requestURI).header("X-Csrftoken", cookieMap.get("bk_csrftoken")).header("Cookie", getCookieStr(cookieMap)).header("Content-Type", "application/json");
        List<String> headerNames = uniresetRequest.getHeaderNames();
        headerNames.forEach(n -> request.header(n, httpRequest.getHeader(n)));
        request.body(jsonObject.toString());
        return request.asJson();
    }

    private HttpResponse<JsonNode> get(UniresetRequest uniresetRequest) {
        String requestURI = uniresetRequest.getRealUrl();
        Map<String, String> cookieMap = uniresetRequest.getCookieMap();
        HttpServletRequest httpRequest = uniresetRequest.getHttpRequest();
        GetRequest request = Unirest.get((uniresetRequest.getOpen() ? openUrl : baseUrl) + requestURI).header("X-Csrftoken", cookieMap.get("bk_csrftoken")).header("Cookie", getCookieStr(cookieMap));
        List<String> headerNames = uniresetRequest.getHeaderNames();
        headerNames.forEach(n -> request.header(n, httpRequest.getHeader(n)));
        return request.asJson();
    }

    private HttpResponse<JsonNode> delete(UniresetRequest uniresetRequest) {
        String requestURI = uniresetRequest.getRealUrl();
        HttpRequestWithBody request = Unirest.delete((uniresetRequest.getOpen() ? openUrl : baseUrl) + requestURI).header("X-Csrftoken", uniresetRequest.getCookieMap().get("bk_csrftoken")).header("Cookie", getCookieStr(uniresetRequest.getCookieMap()));
        List<String> headerNames = uniresetRequest.getHeaderNames();
        headerNames.forEach(n -> request.header(n, request.getHeaders().get(n).get(0)));
        request.body(uniresetRequest.getJsonObject().toString());
        return request.asJson();
    }


    public String getCookieStr(Map<String, String> cookieMap) {
        return cookieMap.entrySet().stream().map(n -> n.getKey() + "=" + n.getValue()).collect(Collectors.joining(";"));
    }

}
