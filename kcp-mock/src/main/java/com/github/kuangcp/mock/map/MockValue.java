package com.github.kuangcp.mock.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/**
 * Mock int long float double String
 *
 * @author https://github.com/kuangcp on 2019-07-14 12:50
 */
public class MockValue {

  private static final Map<Class<?>, Function> typeMap = new HashMap<>();

  private static <T> void put(Class<T> type, Function<T, T> value) {
    typeMap.put(type, value);
  }

  static {
    Function<Integer, Integer> nextInt = ThreadLocalRandom.current()::nextInt;
    put(Integer.class, nextInt);
    put(int.class, nextInt);

    Function<Long, Long> nextLong = ThreadLocalRandom.current()::nextLong;
    put(Long.class, nextLong);
    put(long.class, nextLong);

    Function<Double, Double> nextDouble = ThreadLocalRandom.current()::nextDouble;
    put(Double.class, nextDouble);
    put(double.class, nextDouble);

    put(Float.class, MockValue::mockFloat);
    put(float.class, MockValue::mockFloat);

    put(String.class, MockValue::mockString);
  }

  private static Float mockFloat(Float bound) {
    return (float) ThreadLocalRandom.current().nextDouble(bound);
  }

  private static String mockString(String ignore) {
    return UUID.randomUUID().toString();
  }

  public static boolean isSupportType(Class<?> type) {
    return Objects.nonNull(typeMap.get(type));
  }

  @SuppressWarnings("unchecked")
  public static <T> T mock(T bound) {
    Class<?> type = bound.getClass();
    if (!isSupportType(type)) {
      throw new UnsupportedOperationException(type.getName());
    }

    return (T) typeMap.get(type).apply(bound);
  }

  @SuppressWarnings("unchecked")
  public static <T> T mock(Class<T> type) {
    if (!isSupportType(type)) {
      throw new UnsupportedOperationException(type.getName());
    }

    Object bound;
    String simpleName = type.getSimpleName();
    if (simpleName.equalsIgnoreCase("String")) {
      bound = "";
    } else if (simpleName.equalsIgnoreCase("Long")) {
      bound = 10000L;
    } else if (simpleName.equalsIgnoreCase("Double")) {
      bound = 10000.0d;
    } else if (simpleName.equalsIgnoreCase("Float")) {
      bound = 10000.0f;
    } else if (simpleName.equalsIgnoreCase("Integer")
        || simpleName.equalsIgnoreCase("int")) {
      bound = 10000;
    } else {
      throw new UnsupportedOperationException(type.getName());
    }

    return (T) typeMap.get(type).apply(bound);
  }
}
