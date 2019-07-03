package com.qianbao.print.common.exception;


import com.google.common.collect.Lists;
import com.qianbao.print.common.dto.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * 全局异常处理
 */
@Configuration
@RestControllerAdvice(annotations = RestController.class)
public class BusinessExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessExceptionHandler.class);

    @ExceptionHandler({BusinessException.class})
    public Result handlerBusException(BusinessException ex) {
        LOGGER.warn("[Exception Handler] Business error: {}", ex.getMessage());
        return Result.error(ex.getErrorCode().code(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handlerException(Exception e) {

        LOGGER.warn("[Exception Handler] interval error info :{} ", e.getMessage());
        LOGGER.warn("[Exception Handler] interval error stack info :{} ",
                getStackTraceAsString(e));
        return Result.error();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handlerException(MethodArgumentNotValidException e) {
        LOGGER.warn("[Exception Handler] Bad request: {}", e.getMessage());
        return Result.error("非法参数,请检查参数是否正确！", extractErrors(e.getBindingResult().getFieldErrors()));
    }

    @ExceptionHandler(BindException.class)
    protected Result handleBindException(BindException e) {
        LOGGER.warn("[Exception Handler] Bad request: {}", e.getMessage());
        return Result.error("非法参数,请检查参数是否正确！", extractErrors(e.getBindingResult().getFieldErrors()));
    }

    private List<ErrorField> extractErrors(List<FieldError> errors) {
        List<ErrorField> result = Lists.newArrayList();
        for (FieldError each : errors) {
            result.add(new ErrorField(each.getField(), each.getDefaultMessage()));
        }
        return result;
    }

    private String getStackTraceAsString(Throwable ex) {
        StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
