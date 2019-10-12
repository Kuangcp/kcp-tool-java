package com.github.kuangcp.json;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;

/**
 * instantiate Object with default value by recursion
 *
 * @author https://github.com/kuangcp on 2019-06-13 22:08
 */
@Slf4j
public class InstantiateObjectByClass {

  private static final Date DEFAULT_DATE = new Date();
  private static final BigDecimal DEFAULT_BIG_DECIMAL = new BigDecimal(0.99)
      .setScale(2, RoundingMode.HALF_EVEN);

  private static final Map<Class<?>, Object> typeMap = new HashMap<>();

  private static <T> void put(Class<T> type, T value) {
    typeMap.put(type, value);
  }

  private static Optional<Object> get(Class type) {
    return Optional.ofNullable(typeMap.get(type));
  }

  static {
    put(Integer.class, 0);
    put(int.class, 0);
    put(Long.class, 0L);
    put(long.class, 0L);
    put(Double.class, 0d);
    put(double.class, 0d);
    put(Boolean.class, true);
    put(boolean.class, true);
    put(Float.class, 0f);
    put(float.class, 0f);

    put(String.class, "string");
    put(Date.class, DEFAULT_DATE);
    put(List.class, Collections.emptyList());
    put(BigDecimal.class, DEFAULT_BIG_DECIMAL);
  }

  /**
   * set value for every field with recursion then invoke function that input param
   *
   * @param function Object to String function
   */
  public static <T> String fillFieldsThenToString(Class<T> target, Function<T, String> function) {
    return fillFields(target).map(function).orElse("");
  }

  /**
   * set value for every field with recursion then invoke Object.toString method
   */
  public static <T> String fillFieldsThenToString(Class<T> target) {
    return fillFieldsThenToString(target, Objects::toString);
  }

  /**
   * set value for every field with recursion
   */
  public static <T> Optional<T> fillFields(Class<T> target) {
    try {
      T object = target.newInstance();
      fillFieldsWithObject(object);

      Class<? super T> superclass = target.getSuperclass();
      log.debug("super class {}", superclass.getName());
      fillFieldsWithObject(object, superclass);

      return Optional.of(object);
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }

    return Optional.empty();
  }

  private static <T> void fillFieldsWithObject(T object)
      throws IllegalAccessException, InstantiationException {
    Class<?> target = object.getClass();
    fillFieldsWithObject(object, target);
  }

  private static <T> void fillFieldsWithObject(T object, Class<?> target)
      throws IllegalAccessException, InstantiationException {
    Field[] fields = target.getDeclaredFields();
    fillFieldsWithFields(object, fields);

    Field[] inheritFields = target.getFields();
    fillFieldsWithFields(object, inheritFields);
  }

  private static <T> void fillFieldsWithFields(T object, Field[] fields)
      throws IllegalAccessException, InstantiationException {
    for (Field field : fields) {
      Class<?> type = field.getType();
      field.setAccessible(true);

      Object cache = field.get(object);
      if (Objects.nonNull(cache)) {
        continue;
      }

      Optional<Object> valueOpt = get(type);
      if (valueOpt.isPresent()) {
        field.set(object, valueOpt.get());
      } else if (!type.getName().startsWith("java")) {
        Optional<?> value = fillFields(type.newInstance().getClass());
        if (value.isPresent()) {
          field.set(object, value.get());
        } else {
          log.error("init sub field error: type={}", type);
        }
      } else {
        log.error("not match any type: type={}", type);
      }
    }
  }
}
