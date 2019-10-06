package com.github.kuangcp.aop.aspect;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp on 2019-10-06 18:51
 */
@Slf4j
public class PermissionAspect extends SimpleAspect {

  @Override
  public boolean before(Object target, Method method, Object[] args) {
    log.info("target={} method={} args={}", target, method, args);

    String threadName = Thread.currentThread().getName();
    if (!"main".equals(threadName)) {
      log.warn("must invoke by main thread: currentThread={}", Thread.currentThread());
      return false;
    }

    return super.before(target, method, args);
  }
}
