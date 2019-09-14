package com.github.kuangcp.mock;

import com.github.kuangcp.mock.common.MockUsuallyValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp on 2019-07-14 15:22
 */
@Slf4j
public class MockList {

  public static <T> List<T> mock(long size, T bound) {
    return mock(new ArrayList<>((int) size), size, bound);
  }

  public static <T> List<T> mock(long size, Class<T> type) {
    return mock(new ArrayList<>((int) size), size, MockUsuallyValue.mock(type));
  }

  public static <T> List<T> mock(List<T> list, long size, T bound) {
    if (!MockUsuallyValue.isSupportType(bound.getClass())) {
      log.error("not support type: key={}", bound.getClass().getName());
      return Collections.emptyList();
    }

    try {
      for (int i = 0; i < size; i++) {
        list.add(MockUsuallyValue.mock(bound));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  public static <T> List<T> mock(List<T> list, long size, Class<T> type) {
    return mock(list, size, MockUsuallyValue.mock(type));
  }
}
