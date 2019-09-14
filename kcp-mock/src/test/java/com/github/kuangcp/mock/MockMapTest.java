package com.github.kuangcp.mock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-07-14 13:10
 */
@Slf4j
public class MockMapTest {

  @Test
  public void testMockData() {
    Map<String, Integer> result = MockMap.mock(2, String.class, int.class);
    assertThat(result.size(), equalTo(2));
    result.forEach((key, value) -> log.info("key={} value={}", key, value));
  }

  @Test
  public void testMockLittle(){
    Map<Integer, Integer> result = MockMap.mock(10, 3, 3);
    result.forEach((key, value) -> log.info("key={} value={}", key, value));
  }
}
