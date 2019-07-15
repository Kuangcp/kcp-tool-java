package com.github.kuangcp.bean;

import java.lang.reflect.Method;

/**
 * @author https://github.com/kuangcp on 2019-07-15 11:05
 */
public class GenerateBeanCopierCode {

  /**
   * get and set the same name field
   *
   * @param target class
   * @param set set.setXxx()
   * @param get get.getXxx()
   */
  public static void generate(Class target, String set, String get) {
    for (Method method : target.getDeclaredMethods()) {
      String methodName = method.getName();
      if (methodName.startsWith("get")) {
        String setMethod = methodName.replaceAll("^get", "set");
        System.out.println(String.format("%s.%s(%s.%s());", set, setMethod, get, methodName));
      }
    }
  }
}
