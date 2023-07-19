package com.zhanxin.tbiops.tbiops.dto;

import lombok.Data;

/**
 * @author by fengww
 * @Classname JsonResponse
 * @Description
 * @Date 2023/7/19 22:03
 */
@Data
public class JsonResponse<T> {


    private String errorCode;

    private String errorMsg;

    private T data;


    public static <T> JsonResponse<T> success(T data) {
        JsonResponse<T> jsonResponse = new JsonResponse<>();
        jsonResponse.setData(data);
        jsonResponse.setErrorCode("0");
        jsonResponse.setErrorMsg("success");
        return jsonResponse;
    }


    public static <T> JsonResponse<T> fail(String errorCode, String errorMsg) {
        JsonResponse<T> jsonResponse = new JsonResponse<>();
        jsonResponse.setErrorCode(errorCode);
        jsonResponse.setErrorMsg(errorMsg);
        return jsonResponse;
    }


}
