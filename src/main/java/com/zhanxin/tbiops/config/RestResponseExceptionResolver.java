package com.zhanxin.tbiops.config;

import com.zhanxin.tbiops.dto.JsonException;
import com.zhanxin.tbiops.dto.JsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

@RestControllerAdvice
@Slf4j
public class RestResponseExceptionResolver extends ExceptionHandlerExceptionResolver {

    @ExceptionHandler(JsonException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public JsonResponse handleAccessDeniedException(JsonException e) {
        log.error(ExceptionUtils.getStackTrace(e));
        return JsonResponse.fail(e.getErrorCode(), e.getErrorMsg());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    public JsonResponse handleException(Exception e) {
        log.error(ExceptionUtils.getStackTrace(e));
        return JsonResponse.fail("1001", e.getMessage());
    }

}