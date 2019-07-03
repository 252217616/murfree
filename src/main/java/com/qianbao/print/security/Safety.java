package com.qianbao.print.security;


import com.qianbao.print.common.dto.BodyForm;
import com.qianbao.print.common.dto.SafetyForm;

import java.lang.annotation.*;

/**
 * @author 🐝 King 👉 wangwei2@qianbao.com
 * @create 2018/10/12
 * @copyright Copyright © qianbao.com 2018. All Rights Reserved
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Safety {

  String cipherProperty() default "reqData";

  String organizationProperty() default "organizationNo";

  Class<? extends SafetyForm> target() default BodyForm.class;

}
