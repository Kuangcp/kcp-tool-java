package com.github.kuangcp.mock.common;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mock int long float double String
 *
 * @author https://github.com/kuangcp on 2019-07-14 12:50
 */
public class MockValue {

  private static final String DEFAULT_STR_LEN = "8";
  private static final Map<Class<?>, Function> typeMap = new HashMap<>(10);

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
    put(Date.class, MockValue::mockDate);
    put(BigDecimal.class, MockValue::mockBigDecimal);
  }

  private static BigDecimal mockBigDecimal(BigDecimal bigDecimal) {
    return BigDecimal.valueOf(mock(bigDecimal.doubleValue()));
  }

  private static Date mockDate(Date date) {
    long time = ThreadLocalRandom.current().nextLong(date.getTime() + mock(100000L));
    return new Date(time);
  }

  private static Float mockFloat(Float bound) {
    return (float) ThreadLocalRandom.current().nextDouble(bound);
  }

  private static String mockString(String lenBound) {
    if (Objects.isNull(lenBound)) {
      throw new RuntimeException("should input integer");
    }

    Pattern pattern = Pattern.compile("^[0-9]+$");
    Matcher matcher = pattern.matcher(lenBound);
    if (matcher.find()) {
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < Integer.parseInt(lenBound); i++) {
        int value = ThreadLocalRandom.current().nextInt(65, 123);
        builder.append((char) value);
      }
      return builder.toString();
    } else {
      throw new RuntimeException("should input integer");
    }
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
    Objects.requireNonNull(type);

    if (!isSupportType(type)) {
      throw new UnsupportedOperationException(type.getName());
    }

    Object bound;
    String simpleName = type.getSimpleName();

    switch (simpleName.toLowerCase()) {
      case "string":
        bound = DEFAULT_STR_LEN;
        break;
      case "long":
        bound = Long.MAX_VALUE;
        break;
      case "double":
        bound = Double.MAX_VALUE;
        break;
      case "float":
        bound = Float.MAX_VALUE;
        break;
      case "integer":
      case "int":
        bound = Integer.MAX_VALUE;
        break;
      case "date":
        bound = new Date();
        break;
      case "bigdecimal":
        bound = BigDecimal.valueOf(Double.MAX_VALUE);
        break;
      default:
        throw new UnsupportedOperationException(type.getName());
    }

    return (T) typeMap.get(type).apply(bound);
  }
}
