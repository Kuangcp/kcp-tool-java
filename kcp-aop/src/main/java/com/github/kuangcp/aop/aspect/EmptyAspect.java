package com.github.kuangcp.aop.aspect;

import java.lang.reflect.Method;

/**
 * 空实现的切面模板
 *
 * @author https://github.com/kuangcp on 2019-09-29 01:06
 */
public class EmptyAspect implements Aspect {

  @Override
  public boolean before(Object target, Method method, Object[] args) {
    return true;
  }

  @Override
  public boolean after(Object target, Method method, Object[] args, Object returnVal) {
    return true;
  }

  @Override
  public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
    return true;
  }
}
