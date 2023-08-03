package com.zhanxin.tbiops.http.acl;

import com.zhanxin.tbiops.common.JsonUtils;
import com.zhanxin.tbiops.dto.JsonException;
import kong.unirest.*;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BkRequestService {

    @Autowired
    private BkTokenService bkTokenService;


    @Autowired
    private ErrorMessageService errorMessageService;


    @Value("${bk.base.app_code}")
    private String appCode;

    @Value("${bk.base.app_secret}")
    private String appSecret;

    @Value("${bk.base.open_url}")
    private String openUrl;


    public Object openRequestUrl(HttpServletRequest httpRequest, Map jsonObject) {
        String token = httpRequest.getParameter("token");
        jsonObject.put("bk_app_code", appCode);
        jsonObject.put("bk_app_secret", appSecret);
        Map<String, String> cookieMap = bkTokenService.getCookieMap(token);
        jsonObject.put("bk_token", cookieMap.get("bk_token"));
        String method = httpRequest.getMethod();
        HttpResponse<JsonNode> httpResponse = getObject(httpRequest, jsonObject, token, cookieMap, method, true);
        Cookies cookies = httpResponse.getCookies();
        bkTokenService.updateCookieMap(token, cookies);
        JSONObject object = httpResponse.getBody().getObject();
        if (object.has("data")) {
            Object data = object.get("data");
            if (data instanceof JSONObject) {
                return JsonUtils.toObject(object.getJSONObject("data").toString(), Map.class);
            }
            if (data instanceof JSONArray) {
                return JsonUtils.toObject(object.getJSONArray("data").toString(), List.class);
            }
            return data;
        }
        return object;
    }

    public Object privateRequestUrl(HttpServletRequest httpRequest, Map jsonObject) {
        String token = httpRequest.getParameter("token");
        Map<String, String> cookieMap = bkTokenService.getPrivateCookieMap(token);
        String method = httpRequest.getMethod();
        HttpResponse<JsonNode> httpResponse = getObject(httpRequest, jsonObject, token, cookieMap, method, false);
        Cookies cookies = httpResponse.getCookies();
        bkTokenService.updateCookieMap(token, cookies);
        JSONObject object = httpResponse.getBody().getObject();
        if (object.has("data")) {
            Object data = object.get("data");
            if (data instanceof JSONObject) {
                return JsonUtils.toObject(object.getJSONObject("data").toString(), Map.class);
            }
            if (data instanceof JSONArray) {
                return JsonUtils.toObject(object.getJSONArray("data").toString(), List.class);
            }
            return data;
        }
        return object;
    }

    private HttpResponse<JsonNode> getObject(HttpServletRequest httpRequest, Map jsonObject, String token, Map<String, String> cookieMap, String method, boolean open) {
        HttpResponse<JsonNode> jsonNodeHttpResponse = getJsonNodeHttpResponse(httpRequest, jsonObject, token, cookieMap, method, open);
        JSONObject object = jsonNodeHttpResponse.getBody().getObject();
        Map resultMap = JsonUtils.toObject(object.toString(), Map.class);

        if (resultMap.get("result").equals(false)) {
            if (resultMap.containsKey("bk_error_code")) {
                String bkErrorMsg = errorMessageService.getCnMessage(resultMap.getOrDefault("bk_error_msg", "").toString(), httpRequest.getRequestURL().toString());
                throw new JsonException(resultMap.getOrDefault("bk_error_code", "").toString(), bkErrorMsg);
            }

            String message = errorMessageService.getCnMessage(resultMap.getOrDefault("message", "").toString(), httpRequest.getRequestURL().toString());
            throw new JsonException(resultMap.getOrDefault("code", "").toString(), message);
        }
        return jsonNodeHttpResponse;
    }

    private HttpResponse<JsonNode> getJsonNodeHttpResponse(HttpServletRequest httpRequest, Map jsonObject, String token, Map<String, String> cookieMap, String method, boolean open) {
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
        Map jsonObject = uniresetRequest.getJsonObject();
        Map<String, String> cookieMap = uniresetRequest.getCookieMap();
        requestURI = requestURI.replace("bkapi/open/api/", "");
        HttpRequestWithBody request = Unirest.put((uniresetRequest.getOpen() ? openUrl : uniresetRequest.baseUrl()) + requestURI).header("X-Csrftoken", cookieMap.get("bk_csrftoken")).header("Cookie", getCookieStr(cookieMap));
        List<String> headerNames = uniresetRequest.getHeaderNames();
        headerNames.forEach(n -> request.header(n, httpRequest.getHeader(n)));
        return request.body(JsonUtils.toJsonStirng(jsonObject)).asJson();
    }


    private HttpResponse<JsonNode> post(UniresetRequest uniresetRequest) {
        HttpServletRequest httpRequest = uniresetRequest.getHttpRequest();
        Map jsonObject = uniresetRequest.getJsonObject();
        Map<String, String> cookieMap = uniresetRequest.getCookieMap();
        String requestURI = uniresetRequest.getRealUrl();
        HttpRequestWithBody request = Unirest.post(((uniresetRequest.getOpen() ? openUrl : uniresetRequest.baseUrl())) + requestURI).header("Cookie", getCookieStr(cookieMap)).header("Content-Type", "application/json");
        if (StringUtils.isNotBlank(cookieMap.get("bk_csrftoken"))) {
            request.header("X-Csrftoken", cookieMap.get("bk_csrftoken"));
        }
        List<String> headerNames = uniresetRequest.getHeaderNames();
        headerNames.forEach(n -> {
            if (httpRequest.getHeader(n) != null) {
                request.header(n, httpRequest.getHeader(n));
            }
        });
        RequestBodyEntity body = request.body(JsonUtils.toJsonStirng(jsonObject));
        return body.asJson();
    }

    private HttpResponse<JsonNode> get(UniresetRequest uniresetRequest) {
        String requestURI = uniresetRequest.getRealUrl();
        Map<String, String> cookieMap = uniresetRequest.getCookieMap();
        HttpServletRequest httpRequest = uniresetRequest.getHttpRequest();
        GetRequest request = Unirest.get((uniresetRequest.getOpen() ? openUrl : uniresetRequest.baseUrl()) + requestURI).header("X-Csrftoken", cookieMap.get("bk_csrftoken")).header("Cookie", getCookieStr(cookieMap));
        List<String> headerNames = uniresetRequest.getHeaderNames();
        headerNames.forEach(n -> request.header(n, httpRequest.getHeader(n)));
        return request.asJson();
    }

    private HttpResponse<JsonNode> delete(UniresetRequest uniresetRequest) {
        String requestURI = uniresetRequest.getRealUrl();
        HttpRequestWithBody request = Unirest.delete((uniresetRequest.getOpen() ? openUrl : uniresetRequest.baseUrl()) + requestURI).header("X-Csrftoken", uniresetRequest.getCookieMap().get("bk_csrftoken")).header("Cookie", getCookieStr(uniresetRequest.getCookieMap()));
        List<String> headerNames = uniresetRequest.getHeaderNames();
        HttpServletRequest httpRequest = uniresetRequest.getHttpRequest();
        headerNames.forEach(n -> request.header(n, httpRequest.getHeader(n)));
        return request.body(JsonUtils.toJsonStirng(uniresetRequest.getJsonObject())).asJson();
    }


    public String getCookieStr(Map<String, String> cookieMap) {
        return cookieMap.entrySet().stream().map(n -> n.getKey() + "=" + n.getValue()).collect(Collectors.joining(";"));
    }

}
