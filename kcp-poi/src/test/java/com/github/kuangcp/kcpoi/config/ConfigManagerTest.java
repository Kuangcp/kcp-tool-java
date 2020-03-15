package com.github.kuangcp.kcpoi.config;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-12-12 08:10
 */
public class ConfigManagerTest {

  /**
   * 测试 读物 yml 文件, 实例化 MainConfig
   */
  @Test
  public void testRead() {
    MainConfig instance = ConfigManager.getInstance();

    Assert.assertNotNull(instance);

    System.out.println(instance.toString());
  }
}