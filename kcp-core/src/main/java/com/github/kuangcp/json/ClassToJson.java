package com.github.kuangcp.json;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * init Object with default value by recursion
 *
 * @author https://github.com/kuangcp
 * @date 2019-06-13 22:08
 */
@Slf4j
public class ClassToJson {

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

    put(String.class, "");
    put(Date.class, new Date());
    put(List.class, new ArrayList(1));
    put(BigDecimal.class, new BigDecimal(0));
  }

  public static <T> Optional<T> fillAllFieldValue(Class<T> target) {
    try {
      T object = target.newInstance();
      fillAllFields(target, object);

      Class<? super T> superclass = target.getSuperclass();
      System.out.println(superclass.getName());
      fillAllFields(superclass, object);

      return Optional.of(object);
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }

    return Optional.empty();
  }

  private static <T> void fillAllFields(Class<T> target, T object)
      throws IllegalAccessException, InstantiationException {
    Field[] fields = target.getDeclaredFields();
    fillFields(object, fields);

    Field[] inheritFields = target.getFields();
    fillFields(object, inheritFields);
  }

  private static <T> void fillFields(T object, Field[] fields)
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
        Optional<?> value = fillAllFieldValue(type.newInstance().getClass());
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
