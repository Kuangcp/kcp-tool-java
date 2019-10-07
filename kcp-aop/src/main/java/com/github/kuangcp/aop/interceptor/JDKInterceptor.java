package com.github.kuangcp.aop.interceptor;

import com.github.kuangcp.aop.aspect.Aspect;
import com.github.kuangcp.aop.util.ReflectionUtil;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp on 2019-10-06 17:54
 */
@Slf4j
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JDKInterceptor implements InvocationHandler, Serializable {

  private static final long serialVersionUID = -2379466447302720538L;

  private Object target;
  private Aspect aspect;

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    final Object target = this.target;
    final Aspect aspect = this.aspect;
    Object result = null;

    if (Objects.isNull(target) || Objects.isNull(aspect)) {
      throw new RuntimeException("target or aspect is null");
    }

    // before
    if (aspect.before(target, method, args)) {
      ReflectionUtil.setAccessible(method);
      try {
        Object realTarget = Modifier.isStatic(method.getModifiers()) ? null : target;
        result = method.invoke(realTarget, args);
      } catch (InvocationTargetException e) {
        // 只捕获业务代码导致的异常，而非反射导致的异常
        if (aspect.afterException(target, method, args, e.getTargetException())) {
          throw e;
        }
      }
    }

    // after
    if (aspect.after(target, method, args, result)) {
      return result;
    }

    return result;
  }
}
