package com.github.kuangcp.aop.proxy;

import com.github.kuangcp.aop.ProxyUtil;
import com.github.kuangcp.aop.aspect.Aspect;
import com.github.kuangcp.aop.interceptor.JDKInterceptor;

/**
 * @author https://github.com/kuangcp on 2019-10-06 18:30
 */
public class JdkProxyFactory extends ProxyFactory {

  private static final long serialVersionUID = -817544713057450519L;

  @Override
  @SuppressWarnings("unchecked")
  public <T> T proxy(T target, Aspect aspect) {
    ClassLoader classLoader = target.getClass().getClassLoader();
    JDKInterceptor interceptor = new JDKInterceptor(target, aspect);
    Class<?>[] interfaces = target.getClass().getInterfaces();

    return (T) ProxyUtil.newProxyInstance(classLoader, interceptor, interfaces);
  }
}
