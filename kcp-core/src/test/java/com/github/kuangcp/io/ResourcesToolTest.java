package com.github.kuangcp.io;

import org.junit.Test;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 3/16/19-7:23 PM
 */
public class ResourcesToolTest {

  @Test
  public void testClose() throws Exception {
    ResourceTool.close(null, null, null);
  }
}
