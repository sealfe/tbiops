package com.zhanxin.tbiops.tbiops.config;

import com.zhanxin.tbiops.tbiops.dto.JsonException;
import com.zhanxin.tbiops.tbiops.dto.JsonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

@RestControllerAdvice
public class RestResponseExceptionResolver extends ExceptionHandlerExceptionResolver {

    @ExceptionHandler(JsonException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public JsonResponse handleAccessDeniedException(JsonException e) {
        return JsonResponse.fail(e.getErrorCode(), e.getErrorMsg());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    public JsonResponse handleException(Exception e) {
        return JsonResponse.fail("1001", e.getMessage());
    }

}