package com.qianbao.print.common.config;

import com.alibaba.fastjson.JSONObject;
import com.qianbao.print.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * Async注解的方法 发生错误时具体处理类
 * @author lujun
 * @date 2019年5月6日10:41:19
 */
@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("Async occurred method: {} has uncaught exception,params:{}", method.getName(), JSONObject.toJSONString(params));

        if (ex instanceof BusinessException) {
            BusinessException restException = (BusinessException) ex;
            log.error("Async occurred RestException{}",restException.getMessage());
        }
        log.error("Async occurred Exception :");
        ex.printStackTrace();
    }
}