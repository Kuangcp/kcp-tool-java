package com.github.kuangcp.bean;

import lombok.Builder;
import lombok.Data;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-07-15 11:12
 */
public class GenerateUsuallyCodeTest {

  @Data
  @Builder
  static class Computer {

    String tag;
    String color;
  }

  @Test
  public void testCopyBySet() {
    GenerateUsuallyCode.generateGetThenSet(Computer.class, "one", "computer");
  }

  @Test
  public void testCopyByBuilder() {
    GenerateUsuallyCode.generateGetThenBuild(Computer.class, "computer");
  }

  @Test
  public void testEnum(){
    GenerateUsuallyCode.generateEnumFromState(Computer.class);
  }
}