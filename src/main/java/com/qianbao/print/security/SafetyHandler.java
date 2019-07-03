package com.qianbao.print.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.qianbao.print.common.dto.BaseForm;
import com.qianbao.print.common.dto.SafetyForm;
import com.qianbao.print.common.utils.ReflectionUtil;
import com.qianbao.print.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Slf4j
@RequiredArgsConstructor
@Component
@Aspect
public class SafetyHandler {

  final SecurityService securityService;

  @SneakyThrows
  @Around(value = "@annotation(safety)")
  public Object aroundMethod(ProceedingJoinPoint joinPoint, Safety safety) {

    Signature signature = joinPoint.getSignature();
    MethodSignature methodSignature = (MethodSignature) signature;
    Method method = methodSignature.getMethod();
    Parameter [] parameters = method.getParameters();
    Class<?> type = null;
    for (Parameter parameter : parameters) {
      if (parameter.getType().getSimpleName().equals(safety.target().getSimpleName())) {
        ParameterizedType argType = (ParameterizedType) parameter.getParameterizedType();
        Type[] types = argType.getActualTypeArguments();
        if (types == null || types.length == 0) {
          type = Object.class;
        } else {
          type = (Class<?>) types[0];
        }
        break;
      }
    }
    Object[] argValues = joinPoint.getArgs();
    //todo 自定义解密
    for (Object arg : argValues) {
      if (arg instanceof SafetyForm) {
        SafetyForm form = (SafetyForm)arg;
        String organizationNo = form.getOrganizationNo();
        String data = ReflectionUtil.getProperty(arg, safety.cipherProperty());
        String json = securityService.decrypt(organizationNo, data);
        log.info("decrypt data:{}", json);
        form.setOrganizationNo(organizationNo);
        Object formObj = JSONObject.parseObject(json,type);
        if(formObj instanceof BaseForm){
          ((BaseForm) formObj).verrfy();
        }
        //打印小票内容数据格式按照业务送的json属性顺序，这里使用fastJson的OrderedField
        form.setData(JSON.parseObject(json, type, new Feature[]{Feature.OrderedField}));
        break;
      }
    }

    return joinPoint.proceed();
  }

}
