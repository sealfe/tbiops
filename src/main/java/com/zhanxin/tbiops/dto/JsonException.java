package com.zhanxin.tbiops.dto;

/**
 * @author by fengww
 * @Classname JsonException
 * @Description
 * @Date 2023/7/20 22:54
 */
public class JsonException extends RuntimeException {

    private String errorCode;

    private String errorMsg;

    public JsonException(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }




}
