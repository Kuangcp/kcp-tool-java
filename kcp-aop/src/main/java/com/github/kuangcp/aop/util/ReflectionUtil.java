package com.github.kuangcp.aop.util;


import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author https://github.com/kuangcp on 2019-10-06 18:03
 */
public class ReflectionUtil {


  /**
   * 获得一个类中所有字段列表，直接反射获取，无缓存
   *
   * @param beanClass 类
   * @return 字段列表
   * @throws SecurityException 安全检查异常
   */
  public static Constructor<?>[] getConstructorsDirectly(Class<?> beanClass)
      throws SecurityException {
    return beanClass.getDeclaredConstructors();
  }

  /**
   * 获得一个类中所有构造列表
   *
   * @param <T> 构造的对象类型
   * @param beanClass 类
   * @return 字段列表
   * @throws SecurityException 安全检查异常
   */
  @SuppressWarnings("unchecked")
  public static <T> Constructor<T>[] getConstructors(Class<T> beanClass) throws SecurityException {
//    Assert.notNull(beanClass);
//    Constructor<?>[] constructors = CONSTRUCTORS_CACHE.get(beanClass);
    Constructor<?>[] constructors = null;
//    if (null != constructors) {
//      return (Constructor<T>[]) constructors;
//    }

    constructors = getConstructorsDirectly(beanClass);
    return (Constructor<T>[]) constructors;
//    return (Constructor<T>[]) CONSTRUCTORS_CACHE.put(beanClass, constructors);
  }

  /**
   * 查找类中的指定参数的构造方法，如果找到构造方法，会自动设置可访问为true
   *
   * @param <T> 对象类型
   * @param clazz 类
   * @param parameterTypes 参数类型，只要任何一个参数是指定参数的父类或接口或相等即可，此参数可以不传
   * @return 构造方法，如果未找到返回null
   */
  @SuppressWarnings("unchecked")
  public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {
    if (null == clazz) {
      return null;
    }

    final Constructor<?>[] constructors = getConstructors(clazz);
    Class<?>[] pts;
    for (Constructor<?> constructor : constructors) {
      pts = constructor.getParameterTypes();
      if (ClassUtil.isAllAssignableFrom(pts, parameterTypes)) {
        // 构造可访问
        setAccessible(constructor);
        return (Constructor<T>) constructor;
      }
    }
    return null;
  }

  /**
   * 实例化对象
   *
   * @param <T> 对象类型
   * @param clazz 类
   * @param params 构造函数参数
   * @return 对象
   * @throws RuntimeException 包装各类异常
   */
  public static <T> T newInstance(Class<T> clazz, Object... params) throws RuntimeException {
    if (ArrayUtil.isEmpty(params)) {
      final Constructor<T> constructor = getConstructor(clazz);
      try {
        return constructor.newInstance();
      } catch (Exception e) {
        throw new RuntimeException("Instance class [{}] error!" + clazz, e);
      }
    }

    final Class<?>[] paramTypes = ClassUtil.getClasses(params);
    final Constructor<T> constructor = getConstructor(clazz, paramTypes);
    if (null == constructor) {
      throw new RuntimeException(
          "No Constructor matched for parameter types: [{}]"
              + Arrays.toString(new Object[]{paramTypes}));
    }
    try {
      return constructor.newInstance(params);
    } catch (Exception e) {
      throw new RuntimeException("Instance class [{}] error!" + clazz, e);
    }
  }

  /**
   * 设置方法为可访问（私有方法可以被外部调用）
   *
   * @param <T> AccessibleObject的子类，比如Class、Method、Field等
   * @param accessibleObject 可设置访问权限的对象，比如Class、Method、Field等
   * @since 4.6.8
   */
  public static <T extends AccessibleObject> T setAccessible(T accessibleObject) {
    if (Objects.nonNull(accessibleObject) && !accessibleObject.isAccessible()) {
      accessibleObject.setAccessible(true);
    }
    return accessibleObject;
  }
}
