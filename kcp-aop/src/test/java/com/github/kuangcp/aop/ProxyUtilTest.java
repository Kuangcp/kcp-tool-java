package com.github.kuangcp.aop;

import com.github.kuangcp.aop.aspect.PermissionAspect;
import com.github.kuangcp.aop.represented.Searcher;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-10-06 18:41
 */
@Slf4j
public class ProxyUtilTest {

  @Test
  public void testProxy() throws InterruptedException {
    Searcher proxy = ProxyUtil.proxy(new Searcher(), new PermissionAspect());
    log.info("result={}",proxy.search("who"));

    Thread sub = new Thread(() -> {
      Searcher proxy2 = ProxyUtil.proxy(new Searcher(), new PermissionAspect());
      log.info("result={}",proxy2.search("who"));
    });
    sub.start();

    sub.join();
  }
}
