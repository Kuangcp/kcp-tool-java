package com.github.kuangcp.mock.set;

import com.github.kuangcp.mock.common.MockHelper;
import com.github.kuangcp.mock.map.MockValue;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp on 2019-07-14 15:44
 */
@Slf4j
public class MockSet {

  public static <T> Set<T> mock(long size, T bound) {
    return mock(new HashSet<>((int) size), size, bound);
  }

  public static <T> Set<T> mock(long size, Class<T> type) {
    return mock(new HashSet<>((int) size), size, MockValue.mock(type));
  }

  public static <T> Set<T> mock(Set<T> set, long size, T bound) {
    if (!MockValue.isSupportType(bound.getClass())) {
      log.error("not support type: key={}", bound.getClass().getName());
      return Collections.emptySet();
    }

    try {
      size = MockHelper.min(bound, size);
      while (set.size() < size) {
        set.add(MockValue.mock(bound));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return set;
  }

  public static <T> Set<T> mock(Set<T> set, long size, Class<T> type) {
    return mock(set, size, MockValue.mock(type));
  }
}
