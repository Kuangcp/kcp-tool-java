package com.github.kuangcp.bean;

import java.lang.reflect.Method;

/**
 * @author https://github.com/kuangcp on 2019-07-15 11:05
 */
public class GenerateBeanCopierCode {

  /**
   * get and set the value that field named same
   *
   * @param target class
   * @param set set.setXxx()
   * @param get get.getXxx()
   */
  public static void generateGetAndSet(Class target, String set, String get) {
    for (Method method : target.getDeclaredMethods()) {
      String methodName = method.getName();
      if (methodName.startsWith("get")) {
        String setMethod = methodName.replaceAll("^get", "set");
        System.out.println(String.format("%s.%s(%s.%s());", set, setMethod, get, methodName));
      }
    }
  }

  /**
   * get and set the value that field named same
   */
  public static void generateGetAndBuild(Class target, String get) {
    System.out.println(target.getSimpleName() + ".builder()");
    for (Method method : target.getDeclaredMethods()) {
      String methodName = method.getName();
      if (methodName.startsWith("get")) {
        String property = methodName.replaceAll("^get", "");
        String propertyName = property.substring(0, 1).toLowerCase() + property.substring(1);
        System.out.println(String.format(".%s(%s.%s())", propertyName, get, methodName));
      }
    }
    System.out.println(".build();");
  }
}
