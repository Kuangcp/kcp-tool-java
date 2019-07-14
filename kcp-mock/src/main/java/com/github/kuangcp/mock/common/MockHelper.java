package com.github.kuangcp.mock.common;

import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp on 2019-07-14 15:39
 */
@Slf4j
public class MockHelper {

  public static <T> long min(T bound, long size) {
    long temp = size;
    String simpleName = bound.getClass().getSimpleName();
    if (simpleName.equalsIgnoreCase("Integer")
        || simpleName.equalsIgnoreCase("int")) {
      size = Math.min(size, (Integer) bound);
    }
    if (simpleName.equalsIgnoreCase("Long")) {
      size = Math.min(size, (Long) bound);
    }

    if (temp != size) {
      log.warn("size less than bound: size={} bound={}", temp, bound);
    }
    return size;
  }
}
