package com.zhanxin.tbiops.api;

import com.zhanxin.tbiops.dto.JsonResponse;
import com.zhanxin.tbiops.http.acl.BkRequestService;
import com.zhanxin.tbiops.http.acl.BkTokenService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author by fengww
 * @Classname BaseRequestApi
 * @Description
 * @Date 2023/7/19 21:59
 */
@RequestMapping("bkapi")
@RestController
public class BaseRequestApi {


    @Autowired
    private BkTokenService bkTokenService;

    @Autowired
    private BkRequestService bkRequestService;


    @RequestMapping("login")
    public JsonResponse<String> login(@RequestParam String username, @RequestParam String password) {
        return JsonResponse.success(bkTokenService.getToken(username, password));
    }


    @GetMapping("bk_token")
    public JsonResponse<String> bkToken(@RequestParam String token) {
        return JsonResponse.success(bkTokenService.getBkToken(token));
    }


    @GetMapping("crsf_token")
    public JsonResponse<String> crsfToken(@RequestParam String token) {
        return JsonResponse.success(bkTokenService.getCrsfToken(token));
    }




    @RequestMapping("console/get_version_info/")
    public JsonResponse<Object> getCookie(@RequestParam String token) {
        return JsonResponse.success(bkTokenService.requestUrl("console/get_version_info/", token));
    }


    @SneakyThrows
    @RequestMapping("open/api/**")
    public JsonResponse<Object> openApi(HttpServletRequest httpRequest, @RequestBody(required = false) Map map) {


        return JsonResponse.success(bkRequestService.openRequestUrl(httpRequest, map));
    }

    @RequestMapping("private/api/**")
    public JsonResponse<Object> privateApi(HttpServletRequest httpRequest, @RequestBody(required = false) Map jsonObject) {
        return JsonResponse.success(bkRequestService.privateRequestUrl(httpRequest, jsonObject));
    }


}
