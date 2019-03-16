package com.github.kuangcp.resources;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 3/16/19-7:20 PM
 */
public class ResourcesTool {

  public static void close(Closeable... resources) throws IOException {
    if (Objects.isNull(resources)) {
      return;
    }
    for (Closeable closeable : resources) {
      if (Objects.isNull(closeable)) {
        continue;
      }
      closeable.close();
    }
  }
}
