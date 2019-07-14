package com.github.kuangcp.mock.map;

import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-07-14 12:54
 */
public class MockValueTest {


  @Test
  public void testMock() {
    Integer mock = MockValue.mock(100);
    System.out.println(mock);

    Double value = MockValue.mock(30.0);
    System.out.println(value);

    String dfdf = MockValue.mock("dfdf");
    System.out.println(dfdf);
  }

  @Test
  public void testMockByType() {
    System.out.println(MockValue.mock(String.class));
    System.out.println(MockValue.mock(int.class));
    System.out.println(MockValue.mock(double.class));
    System.out.println(MockValue.mock(long.class));
    System.out.println(MockValue.mock(float.class));
  }
}
