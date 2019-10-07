package com.github.kuangcp.aop;

import com.github.kuangcp.aop.aspect.Aspect;
import com.github.kuangcp.aop.proxy.ProxyFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 代理工具
 *
 * @author https://github.com/kuangcp on 2019-09-28 20:00
 */
public class ProxyUtil {

  /**
   * 使用切面代理对象
   *
   * @param <T> 切面对象类型
   * @param target 目标对象
   * @param aspectClass 切面对象类
   * @return 代理对象
   */
  public static <T> T proxy(T target, Class<? extends Aspect> aspectClass) {
    return ProxyFactory.createProxy(target, aspectClass);
  }

  /**
   * 使用切面代理对象
   *
   * @param <T> 被代理对象类型
   * @param aspect 切面对象
   * @return 代理对象
   */
  public static <T> T proxy(T target, Aspect aspect) {
    return ProxyFactory.createProxy(target, aspect);
  }

  /**
   * 基于JDK创建动态代理对象
   *
   * @param classloader 当前类加载器
   * @param invocationHandler 实现  InvocationHandler 接口的拦截器
   * @param interfaces 被代理类所实现的接口，为了让代理对象进行实现
   * @return 代理对象
   */
  @SuppressWarnings("unchecked")
  public static <T> T newProxyInstance(ClassLoader classloader, InvocationHandler invocationHandler,
      Class<?>... interfaces) {
    return (T) Proxy.newProxyInstance(classloader, interfaces, invocationHandler);
  }
}
