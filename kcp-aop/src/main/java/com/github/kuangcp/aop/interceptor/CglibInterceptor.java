package com.github.kuangcp.aop.interceptor;

import com.github.kuangcp.aop.aspect.Aspect;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author https://github.com/kuangcp on 2019-10-06 18:37
 */
@Slf4j
@Getter
@AllArgsConstructor
public class CglibInterceptor implements MethodInterceptor, Serializable {

  private static final long serialVersionUID = -7058317844967579327L;

  private Object target;
  private Aspect aspect;

  private CglibInterceptor() {
  }

  @Override
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
      throws Throwable {
    Object result = null;

    // 开始前回调
    if (aspect.before(target, method, args)) {
      try {
        result = proxy.invokeSuper(obj, args);
      } catch (InvocationTargetException e) {
        // 异常回调（只捕获业务代码导致的异常，而非反射导致的异常）
        if (aspect.afterException(target, method, args, e.getTargetException())) {
          throw e;
        }
      }
    }

    // 结束执行回调
    if (aspect.after(target, method, args, result)) {
      return result;
    }
    return null;
  }
}
