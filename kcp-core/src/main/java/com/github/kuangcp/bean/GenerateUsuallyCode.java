package com.github.kuangcp.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author https://github.com/kuangcp on 2019-07-15 11:05
 */
public class GenerateUsuallyCode {

  /**
   * get and set the value that field named same
   *
   * @param target class
   * @param set set.setXxx()
   * @param get get.getXxx()
   */
  public static void generateGetThenSet(Class target, String set, String get) {
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
  public static void generateGetThenBuild(Class target, String get) {
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

  public static void generateEnumFromInterface(Class target) {
    StringBuilder result = new StringBuilder();
    Field[] declaredFields = target.getDeclaredFields();
    if (declaredFields.length == 0) {
      return;
    }

    System.out.println("@AllArgsConstructor\npublic enum " + target.getSimpleName() + "Enum {\n");
    String className = target.getSimpleName();
    for (Field field : declaredFields) {
      String name = field.getName();
      result.append("    ").append(name).append("(").append(className).append(".").append(name)
          .append(", \"\"),\n");
    }
    System.out.println(result.toString().substring(0, result.length() - 2) + ";");

    System.out.println("\n    @Getter\n"
        + "    private int index;\n"
        + "\n"
        + "    @Getter\n"
        + "    private String desc;\n}");
  }

  public static void generateSQLFromEntity(Class target) {
    StringBuilder result = new StringBuilder();
    Field[] declaredFields = target.getDeclaredFields();
    if (declaredFields.length == 0) {
      return;
    }

    String className = target.getSimpleName();
    System.out.println("CREATE TABLE " + className + " (");
    for (Field field : declaredFields) {
      String name = field.getName();

      String type = "";

      Class<?> classType = field.getType();
      if (String.class.equals(classType)) {
        type = " varchar(64) ";
      } else if (Integer.class.equals(classType)) {
        type = " int ";
      } else if (Long.class.equals(classType)) {
        type = " bigint ";
      } else if (Date.class.equals(classType)) {
        type = " timestamp ";
      }

      result.append(name).append(type).append(" null comment '',\n");
    }

    System.out.println(result.toString().substring(0, result.length() - 2)
        + ")\n comment '表' charset = utf8;");
  }
}
