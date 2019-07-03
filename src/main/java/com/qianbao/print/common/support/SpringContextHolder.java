package com.qianbao.print.common.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author 🐝 King 👉 wangwei2@qianbao.com
 * @create 2018/6/27
 * @copyright Copyright © qianbao.com 2018. All Rights Reserved
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {

  /**
   * 上下文对象实例
   */
  private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SpringContextHolder.applicationContext = applicationContext;
  }

  /**
   * 通过name获取 Bean.
   * @param name
   * @return
   */
  public static <T> T getBean(String name){
    return (T)applicationContext.getBean(name);
  }

  /**
   * 通过class获取Bean.
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T getBean(Class<T> clazz){
    return applicationContext.getBean(clazz);
  }

}