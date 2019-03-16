package com.github.kuangcp.resources;

import org.junit.Test;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 3/16/19-7:23 PM
 */
public class ResourcesToolTest {

  @Test
  public void testClose() throws Exception {
    ResourcesTool.close(null, null, null);
  }
}
