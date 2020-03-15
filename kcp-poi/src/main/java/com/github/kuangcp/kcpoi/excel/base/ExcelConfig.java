package com.github.kuangcp.kcpoi.excel.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by https://github.com/kuangcp on 18-2-22  下午3:27
 * 类属性上的注解, 用于Excel的导入导出时设置忽略某些一些字段
 *
 * @author kuangcp
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelConfig {

  /**
   * 是否需要导出, 默认是导出true
   */
  boolean exportFlag() default true;

  /**
   * 列标题
   */
  String value() default "";
}
