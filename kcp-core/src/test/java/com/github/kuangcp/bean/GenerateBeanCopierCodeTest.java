package com.github.kuangcp.bean;

import lombok.Data;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-07-15 11:12
 */
public class GenerateBeanCopierCodeTest {

  @Data
  class Computer {

    String tag;
    String color;
  }

  @Test
  public void testCopyBySet() {
    GenerateBeanCopierCode.generateGetAndSet(Computer.class, "one", "computer");
  }

  @Test
  public void testCopyByBuilder() {
    GenerateBeanCopierCode.generateGetAndBuild(Computer.class, "computer");
  }
}