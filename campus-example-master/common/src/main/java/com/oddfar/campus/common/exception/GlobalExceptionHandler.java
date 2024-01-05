package com.oddfar.campus.common.exception;

import com.oddfar.campus.common.result.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理类
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.fail();
    }

    /**
     * 检测异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result error(MethodArgumentNotValidException e) {
        String message = StringUtils.substringAfterLast(e.getMessage(), "default message").replace("]]", "]");
        message = message.trim();
        return Result.fail(message);
    }

    /**
     * Bind异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Result error(BindException e) {
        String message = StringUtils.substringAfterLast(e.getMessage(), "default message").replace("]]", "]");
        message = message.trim();
        return Result.fail(message);
    }

    /**
     * JSON传输异常处理
     * HttpMessageNotReadableException
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public Result error(HttpMessageNotReadableException e) {
        return Result.fail(e.getMessage());
    }

    /**
     * 自定义异常处理方法
     *
     * @param e
     * @return
     */
    @ExceptionHandler(CampusException.class)
    @ResponseBody
    public Result error(CampusException e) {

        return Result.build(e.getCode(), e.getMessage());
    }
}
