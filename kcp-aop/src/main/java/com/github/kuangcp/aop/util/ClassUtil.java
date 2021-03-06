package com.github.kuangcp.aop.util;

import com.github.kuangcp.aop.proxy.BasicType;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author https://github.com/kuangcp on 2019-10-06 18:17
 */
public abstract class ClassUtil {

  /**
   * 是否为包装类型
   *
   * @param clazz 类
   * @return 是否为包装类型
   */
  public static boolean isPrimitiveWrapper(Class<?> clazz) {
    return Objects.nonNull(clazz) && BasicType.wrapperPrimitiveMap.containsKey(clazz);
  }

  /**
   * 是否为基本类型（包括包装类和原始类）
   *
   * @param clazz 类
   * @return 是否为基本类型
   */
  public static boolean isBasicType(Class<?> clazz) {
    return Objects.nonNull(clazz) && (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
  }

  /**
   * 比较判断types1和types2两组类，如果types1中所有的类都与types2对应位置的类相同，或者是其父类或接口，则返回<code>true</code>
   *
   * @param types1 类组1
   * @param types2 类组2
   * @return 是否相同、父类或接口
   */
  public static boolean isAllAssignableFrom(Class<?>[] types1, Class<?>[] types2) {
    if (ArrayUtil.isEmpty(types1) && ArrayUtil.isEmpty(types2)) {
      return true;
    }
    if (null == types1 || null == types2) {
      // 任何一个为null不相等（之前已判断两个都为null的情况）
      return false;
    }
    if (types1.length != types2.length) {
      return false;
    }

    Class<?> type1;
    Class<?> type2;
    for (int i = 0; i < types1.length; i++) {
      type1 = types1[i];
      type2 = types2[i];
      if (isBasicType(type1) && isBasicType(type2)) {
        // 原始类型和包装类型存在不一致情况
        if (BasicType.unWrap(type1) != BasicType.unWrap(type2)) {
          return false;
        }
      } else if (!type1.isAssignableFrom(type2)) {
        return false;
      }
    }
    return true;
  }

  /**
   * 获得对象数组的类数组
   *
   * @param objects 对象数组，如果数组中存在{@code null}元素，则此元素被认为是Object类型
   * @return 类数组
   */
  public static Class<?>[] getClasses(Object... objects) {
    Class<?>[] classes = new Class<?>[objects.length];
    List<Class<?>> result = Stream.of(objects)
        .map(v -> Objects.isNull(v) ? Object.class : v.getClass())
        .collect(Collectors.toList());
    return result.toArray(classes);
  }

  public static ClassLoader getDefaultClassLoader() {
    ClassLoader loader = null;
    try {
      loader = Thread.currentThread().getContextClassLoader();
    } catch (Throwable ignored) {

    }
    if (Objects.nonNull(loader)) {
      return loader;
    }

    loader = ClassUtil.class.getClassLoader();
    if (Objects.nonNull(loader)) {
      return loader;
    }

    try {
      loader = ClassLoader.getSystemClassLoader();
    } catch (Throwable ignored) {

    }
    return loader;
  }
}