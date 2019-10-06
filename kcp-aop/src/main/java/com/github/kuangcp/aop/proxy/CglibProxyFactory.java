package com.github.kuangcp.aop.proxy;

import com.github.kuangcp.aop.aspect.Aspect;
import com.github.kuangcp.aop.interceptor.CglibInterceptor;
import net.sf.cglib.proxy.Enhancer;

/**
 * @author https://github.com/kuangcp on 2019-10-06 18:29
 */
public class CglibProxyFactory extends ProxyFactory {

  private static final long serialVersionUID = 8353794991004615013L;

  @Override
  @SuppressWarnings("unchecked")
  public <T> T proxy(T target, Aspect aspect) {
    final Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(target.getClass());
    enhancer.setCallback(new CglibInterceptor(target, aspect));
    return (T) enhancer.create();
  }
}
