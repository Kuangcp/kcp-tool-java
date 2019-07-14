package com.github.kuangcp.mock.map;

import com.github.kuangcp.mock.common.MockHelper;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp
 * 2019-07-13 09:30
 */
@Slf4j
public class MockMap {

  public static <K, V> Map<K, V> mock(long size, Class<K> key, Class<V> value) {
    return mock(size, MockValue.mock(key), MockValue.mock(value));
  }

  public static <K, V> Map<K, V> mock(long size, Class<K> key, V value) {
    return mock(size, MockValue.mock(key), value);
  }

  public static <K, V> Map<K, V> mock(long size, K key, Class<V> value) {
    return mock(size, key, MockValue.mock(value));
  }

  public static <K, V> Map<K, V> mock(long size, K keyBound, V valueBound) {
    return mock(new HashMap<>((int) size), size, keyBound, valueBound);
  }

  /**
   * custom map
   */
  public static <K, V> Map<K, V> mock(Map<K, V> map, long size, Class<K> key, Class<V> value) {
    return mock(map, size, MockValue.mock(key), MockValue.mock(value));
  }

  /**
   * custom map
   */
  public static <K, V> Map<K, V> mock(Map<K, V> map, long size, K keyBound, V valueBound) {
    if (size <= 0) {
      throw new IllegalArgumentException("size less than 0");
    }

    if (!MockValue.isSupportType(keyBound.getClass())
        || !MockValue.isSupportType(valueBound.getClass())) {
      log.error("not support type: key={} value={}",
          keyBound.getClass().getName(), valueBound.getClass().getName());
      return Collections.emptyMap();
    }

    try {
      size = MockHelper.min(keyBound, size);
      while (map.size() < size) {
        map.put(MockValue.mock(keyBound), MockValue.mock(valueBound));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return map;
  }
}
