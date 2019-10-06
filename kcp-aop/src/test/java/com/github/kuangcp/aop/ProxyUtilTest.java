package com.github.kuangcp.aop;

import com.github.kuangcp.aop.aspect.PermissionAspect;
import com.github.kuangcp.aop.target.Searcher;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-10-06 18:41
 */
public class ProxyUtilTest {

  @Test
  public void testProxy() throws InterruptedException {
    Searcher proxy = ProxyUtil.proxy(new Searcher(), new PermissionAspect());
    proxy.search("who");

    Thread sub = new Thread(() -> {
      Searcher proxy2 = ProxyUtil.proxy(new Searcher(), new PermissionAspect());
      proxy2.search("who");
    });
    sub.start();

    sub.join();
  }
}
