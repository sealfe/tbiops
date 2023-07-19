package com.zhanxin.tbiops.tbiops.api;

import com.zhanxin.tbiops.tbiops.dto.JsonResponse;
import com.zhanxin.tbiops.tbiops.http.acl.BkTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by fengww
 * @Classname BaseRequestApi
 * @Description
 * @Date 2023/7/19 21:59
 */
@RequestMapping("bk_token")
@RestController
public class BaseRequestApi {


    @Autowired
    private BkTokenService bkTokenService;


    @PutMapping("login")
    public JsonResponse<String> login(@RequestParam String username, @RequestParam String password) {
        return JsonResponse.success(bkTokenService.getToken(username, password));
    }


    @RequestMapping("console/get_version_info/")
    public JsonResponse<Object> getCookie(@RequestParam String token) {
        return JsonResponse.success(bkTokenService.requestUrl("console/get_version_info/", token));
    }


}
