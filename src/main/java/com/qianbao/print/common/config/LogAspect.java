package com.qianbao.print.config;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.qianbao.print.security.LogIgnore;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wangwei
 * @create 2018/5/7
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    //todo
    @Pointcut("execution(* com.qianbao.print.web.*Controller.*(..))")
    public void webMethod() {
    }

    //请求method前打印内容
    @Around("webMethod()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String remoteAddr = request.getRemoteAddr();
        String forwardIp = request.getHeader("x-forwarded-for");
        if (forwardIp != null) {
            remoteAddr = forwardIp.split(",")[0];
        }

        MDC.put("ip", remoteAddr);
        MDC.put("port", Integer.toString(request.getRemotePort()));
        MDC.put("threadId", String.valueOf(UUID.randomUUID()));

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] argNames = methodSignature.getParameterNames();
        Object[] argValues = joinPoint.getArgs();

        int len = argNames.length;
        Map<String, Object> args = Maps.newHashMap();
        while (--len >= 0) {
            if(methodSignature.getMethod().getAnnotation(LogIgnore.class) != null){
                args.put(argNames[len], "******");
                continue;
            }
            args.put(argNames[len], argValues[len]);
        }

        log.info("request url:{}, method:{}, args:{}", request.getRequestURL(),
                request.getMethod(), args);
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();

        if(methodSignature.getMethod().getAnnotation(LogIgnore.class) != null){
            log.info("method:{},response use {}ms, result: {}", joinPoint.getSignature().getName(),System.currentTimeMillis() - start, "******");
        }else{
            log.info("method:{},response use {}ms, result: {}", joinPoint.getSignature().getName(),System.currentTimeMillis() - start, result);
        }
        MDC.clear();
        return result;
    }

}

