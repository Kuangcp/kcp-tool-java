package com.github.kuangcp.mock.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * usually used to mock data DTO
 *
 * @author https://github.com/kuangcp on 2019-09-14 13:06
 */
@Slf4j
public class MockObject {

  public static <T> Optional<T> mockObject(Class<T> type) {
    if (MockUsuallyValue.isSupportType(type)) {
      return Optional.of(MockUsuallyValue.mock(type));
    }

    try {
      T result = type.getConstructor().newInstance();
      initFields(result, type);
      return Optional.of(result);
    } catch (Exception e) {
      log.error("", e);
    }
    return Optional.empty();
  }

  private static <T> void initFields(T result, Class<?> type)
      throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
    Method[] methods = type.getMethods();
    for (Method method : methods) {
      if (!method.getName().startsWith("set")) {
        continue;
      }

      Parameter parameter = method.getParameters()[0];
      if (MockUsuallyValue.isSupportType(parameter.getType())) {
        method.invoke(result, MockUsuallyValue.mock(parameter.getType()));
      } else {
        Constructor constructor = parameter.getType().getConstructor();
        Object field = constructor.newInstance();
        method.invoke(result, field);

        initFields(field, parameter.getType());
      }
    }
  }
}
