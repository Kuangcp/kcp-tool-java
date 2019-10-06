package com.github.kuangcp.aop.proxy;

/**
 * @author https://github.com/kuangcp on 2019-10-06 18:22
 */
public class ArrayUtil {

  /**
   * 数组是否为空
   *
   * @param <T> 数组元素类型
   * @param array 数组
   * @return 是否为空
   */
  public static <T> boolean isEmpty(T[] array) {
    return array == null || array.length == 0;
  }

}
