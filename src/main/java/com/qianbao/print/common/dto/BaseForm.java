package com.qianbao.print.common.dto;

import com.qianbao.print.common.exception.BusinessException;
import com.qianbao.print.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class BaseForm {
    public void verrfy() throws IllegalAccessException, InvocationTargetException {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            Object fieldValue = runGetter(field, this);

            boolean isAnnotationNotNull = field.isAnnotationPresent(NotEmpty.class);
            if (isAnnotationNotNull && (fieldValue == null || "".equals(fieldValue))) {
                log.info("[{}] can't be null",fieldName);
                throw BusinessException.of(ErrorCode.INVALID.code(),field.getAnnotation(NotEmpty.class).message());
            }
        }
    }

    // 由于所有子类的属性都是private的，所以必须要找到属性的getter方法
    public Object runGetter(Field field, Object instance) {
        // Find the correct method
        for (Method method : instance.getClass().getDeclaredMethods()) {
            if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3))) {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    // Method found, run it
                    try {
                        return method.invoke(instance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        System.out.println("Could not determine method: " + method.getName());
                    }
                }
            }
        }
        return null;
    }
}
